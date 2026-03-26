package com.zosh.service.serviceImpl;
import com.zosh.config.JwtProvider;
import com.zosh.domain.USER_ROLE;
import com.zosh.model.Cart;
import com.zosh.model.Seller;
import com.zosh.model.User;
import com.zosh.model.VerificationCode;
import com.zosh.repository.CartRepository;
import com.zosh.repository.SellerRepository;
import com.zosh.repository.UserRepository;
import com.zosh.repository.VerificationCodeRepository;
import com.zosh.response.AuthResponse;
import com.zosh.response.SignupRequest;
import com.zosh.service.AuthService;
import com.zosh.service.EmailService;
import com.zosh.request.LoginRequest;
import com.zosh.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final CustomUserServiceImpl customUserService;
    private final EmailService emailService;
    private final SellerRepository sellerRepository;

    @Override
    public void sentLoginOtp(String email, USER_ROLE role) throws Exception {

        String SIGNING_PREFIX="signing_";
      if(email.startsWith(SIGNING_PREFIX)) {
          email = email.substring(SIGNING_PREFIX.length());
      }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
        if (verificationCode == null) {
            verificationCode = new VerificationCode();
            verificationCode.setEmail(email);
        }
        String otp = OtpUtil.generateOtp();
        verificationCode.setOtp(otp);

        if(role.equals(USER_ROLE.ROLE_SELLER)){
            Seller seller =sellerRepository.findByEmail(email);
            if(seller==null){
                throw new Exception("seller not found..");
            }
            verificationCode.setSeller(seller);
            verificationCode.setUser(null);
        }
        else{
            User user=userRepository.findByEmail(email);
            if(user==null) {
                throw new Exception("user not exist with provided email");
            }
            verificationCode.setUser(user);
            verificationCode.setSeller(null);
        }
        verificationCodeRepository.save(verificationCode);
        String subject = "zosh bazaar login/signup otp";
        String text = "your login/signup otp is - " + otp;
        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }

    @Override
    public String createUser(SignupRequest req) throws Exception {
       VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
        if(verificationCode==null || !verificationCode.getOtp().equals(req.getOtp())){
            throw new Exception("wrong otp...");
        }
        User user = userRepository.findByEmail(req.getEmail());
        if(user==null){
            User createdUser = new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("9877653423");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
            user = userRepository.save(createdUser);
            Cart cart =new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(),null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signing(LoginRequest req) throws Exception {
    String username=req.getEmail();
    String otp=req.getOtp();
    Authentication authentication= authenticate(username, otp);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token= jwtProvider.generateToken(authentication);
    AuthResponse authResponse=new AuthResponse();
    authResponse.setJwt(token);
    authResponse.setMessage("Login success.");
    Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();
    String roleName=authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
    authResponse.setRole(USER_ROLE.valueOf(roleName));
    return authResponse;
    }

    private Authentication authenticate(String username, String otp) throws Exception {
       UserDetails userDetails=customUserService.loadUserByUsername(username);
        String SELLER_PREFIX="seller_";
        if(username.startsWith(SELLER_PREFIX)){
        username=username.substring(SELLER_PREFIX.length());

        }
//       UserDetails userDetails=customUserService.loadUserByUsername(username);

    if(userDetails==null){
        throw new BadCredentialsException("invalid username");
    }
    VerificationCode verificationCode=verificationCodeRepository.findByEmail(username);
    if(verificationCode==null || !verificationCode.getOtp().equals(otp)){
        throw new Exception("wrong otp");


    }
    return new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
    );
    }
}
