package egovframework.com.cmm.interceptor;

import com.study.myhome.enums.Authority;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

/**
 * 인증여부 체크 인터셉터
 *
 * @author 공통서비스 개발팀 서준식
 * @version 1.0
 * @see <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2011.07.01  서준식          최초 생성
 *  2011.09.07  서준식          인증이 필요없는 URL을 패스하는 로직 추가
 *  2014.06.11  이기하          인증이 필요없는 URL을 패스하는 로직 삭제(xml로 대체)
 * </pre>
 * @since 2011.07.01
 */

public class NormalUserSessionInterceptor extends SessionCheckInterceptor {

  private final static Logger LOG = LoggerFactory.getLogger(NormalUserSessionInterceptor.class);

  /**
   * 세션에 계정정보(LoginVO)가 있는지 여부로 인증 여부를 체크한다. 계정정보(LoginVO)가 없다면, 로그인 페이지로
   * 이동한다.
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {

    // 로그인 체크
    isLogined();
    // 로그인 세션 객체 가져옴.
    LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

    isNormalUser(loginVO);

    return true;
  }

  private void isNormalUser(LoginVO loginVO) throws ModelAndViewDefiningException {
    if (!loginVO.getUserAuthority().getAuthority().equals(Authority.MEMBER)) {
      LOG.info("user is not normal user");
      ModelAndView modelAndView = new ModelAndView("redirect:/index.do");
      throw new ModelAndViewDefiningException(modelAndView);
    }
  }
}
