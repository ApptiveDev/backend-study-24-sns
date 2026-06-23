package com.example.sns.view;

import com.example.sns.exception.BusinessException;
import com.example.sns.security.util.PasswordEncoder;
import com.example.sns.user.entity.User;
import com.example.sns.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final UserRepository userRepository;

    /**
     * 1단계: 로그인 화면 보여주기
     * GET /login 요청 → "login" 문자열 반환 → templates/login.html 렌더링
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";  // templates/login.html로 이동
    }

    /**
     * 2단계: 로그인 처리
     * POST /login 요청 → AuthService로 검증 → Session에 사용자 정보 저장 → home으로 리다이렉트
     */
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        try {
            // 사용자 찾기
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessException(null));

            // 비밀번호 확인 (암호화된 비밀번호와 비교)
            if (!PasswordEncoder.matches(password, user.getPassword())) {
                throw new BusinessException(null);
            }

            // Session에 사용자 정보 저장 (로그인 상태 유지)
            session.setAttribute("userId", user.getId());
            session.setAttribute("email", user.getEmail());

            // 홈 화면으로 리다이렉트
            return "redirect:/home";

        } catch (Exception e) {
            // 로그인 실패 시 에러 메시지와 함께 로그인 화면으로 돌아감
            model.addAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return "login";
        }
    }

    /**
     * 3단계: 홈 화면 보여주기
     * GET /home 요청 → Session에서 사용자 정보 가져오기 → Model에 담아서 home.html로 전달
     */
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // Session에서 사용자 정보 가져오기
        Long userId = (Long) session.getAttribute("userId");
        String email = (String) session.getAttribute("email");

        // 로그인 안 했으면 로그인 화면으로
        if (userId == null) {
            return "redirect:/login";
        }

        // Model에 데이터 담기 (Thymeleaf가 이 데이터를 HTML에 바인딩)
        model.addAttribute("userId", userId);
        model.addAttribute("email", email);

        return "home";  // templates/home.html로 이동
    }

    /**
     * 4단계: 로그아웃
     * GET /logout 요청 → Session 무효화 → 로그인 화면으로 리다이렉트
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Session 전체 삭제
        return "redirect:/login";
    }

    /**
     * 5단계: 회원가입 화면 보여주기
     * GET /signup 요청 → "signup" 문자열 반환 → templates/signup.html 렌더링
     */
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";  // templates/signup.html로 이동
    }

    /**
     * 6단계: 회원가입 처리
     * POST /signup 요청 → 사용자 생성 → 로그인 화면으로 리다이렉트
     */
    @PostMapping("/signup")
    public String signup(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            Model model
    ) {
        try {
            // 비밀번호 확인
            if (!password.equals(passwordConfirm)) {
                model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
                return "signup";
            }

            // 이메일 중복 체크
            if (userRepository.findByEmail(email).isPresent()) {
                model.addAttribute("error", "이미 가입된 이메일입니다.");
                return "signup";
            }

            // 사용자 생성 (비밀번호 자동 암호화)
            User user = User.create(email, name, password);
            userRepository.save(user);

            // 회원가입 성공 후 로그인 페이지로 리다이렉트
            model.addAttribute("success", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "login";

        } catch (Exception e) {
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다.");
            return "signup";
        }
    }
}