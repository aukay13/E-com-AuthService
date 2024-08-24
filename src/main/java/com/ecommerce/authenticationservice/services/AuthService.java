package com.ecommerce.authenticationservice.services;

import com.ecommerce.authenticationservice.Exceptions.*;
import com.ecommerce.authenticationservice.models.Role;
import com.ecommerce.authenticationservice.models.Session;
import com.ecommerce.authenticationservice.models.SessionStatus;
import com.ecommerce.authenticationservice.models.User;
import com.ecommerce.authenticationservice.repositories.SessionRepository;
import com.ecommerce.authenticationservice.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecretKey key = Keys.hmacShaKeyFor("SomeSafelyStoredSecretKeyWhichIsSupposedToBeASecretButIsNot"
            .getBytes(StandardCharsets.UTF_8));

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean signUp(String email, String password) throws UserAlreadyExistsException {
        if(userRepository.findByEmail(email).isPresent()){
            throw new UserAlreadyExistsException(STR."User with \{email} already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(user);
        return true;
    }

    public String login(String email, String password) throws NoUserFoundException, IncorrectPasswordException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new NoUserFoundException(STR."Username with email - \{email} not found");
        }

        boolean matches = bCryptPasswordEncoder.matches(password,user.get().getPassword());

        if(!matches){
            throw new IncorrectPasswordException("Incorrect password");
        }

        String token = createJWT(user.get().getId(),user.get().getRoles(),email);

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user.get());
        session.setExpiringAt(getDatePlus30Days());

        sessionRepository.save(session);

        return token;
    }


    public boolean validate(String jwtoken) throws InvalidTokenException, TokenExpiredException, UserAlreadyLoggedOutException {
        try {
            Jws<Claims> claims = getClaimsJws(jwtoken);
            Long userId = claims.getPayload().get("user-id",Long.class);
            String email = claims.getPayload().get("email",String.class);
//            Session currentSession = sessionRepository.findByUser_Id(userId);
            Session currentSession = sessionRepository.findByToken(jwtoken);

            Date expiringAt = claims.getPayload().getExpiration();
            if(expiringAt.before(new Date())){
                currentSession.setSessionStatus(SessionStatus.ENDED);
                sessionRepository.save(currentSession);
                throw new TokenExpiredException("");
            }

            if(currentSession.getSessionStatus().equals(SessionStatus.ENDED)){
                throw new UserAlreadyLoggedOutException("");
            }
            return true;
        }
        catch (UserAlreadyLoggedOutException e){
            throw new UserAlreadyLoggedOutException("You have been logged out. Kindly login again");
        }
        catch (TokenExpiredException e){
            throw new TokenExpiredException("Token received is expired. Please login again");
        }
        catch (Exception e){
            throw new InvalidTokenException("Invalid token received");
        }
    }

    public boolean logout(String jwtoken) throws InvalidTokenException, TokenExpiredException, UserAlreadyLoggedOutException {
        if(validate(jwtoken)){
        Jws<Claims> claims = getClaimsJws(jwtoken);
        Long userId = claims.getPayload().get("user-id",Long.class);

        Session currentSession = sessionRepository.findByToken(jwtoken);
        currentSession.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(currentSession);
        return true;
        }
        return false;
    }

    private String createJWT(Long id, Set<Role> roles, String email) {
        Map<String,Object> dataInJWT = new HashMap<>();
        dataInJWT.put("user-id",id);
        dataInJWT.put("roles",roles);
        dataInJWT.put("email",email);

        return Jwts.builder()
                .claims(dataInJWT)
                .expiration(getDatePlus30Days())
                .issuedAt(new Date())
                .signWith(key)
                .compact();
    }

    private static Date getDatePlus30Days() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        return calendar.getTime();
    }

    private Jws<Claims> getClaimsJws(String jwtoken) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtoken);
    }



}
