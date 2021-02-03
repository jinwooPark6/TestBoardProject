package egovframework.example.board.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.example.board.service.BoardService;
import egovframework.example.board.service.BoardVO;
import egovframework.example.sample.service.SampleDefaultVO;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


@Controller
public class BoardController {
	
	@Resource(name = "boardService")
	private BoardService boardService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@RequestMapping(value = "/list.do")
	public String list(@ModelAttribute("boardVO") BoardVO boardVO, ModelMap model) throws Exception {
		
		//boardVO.setPageUnit(propertiesService.getInt("pageUnit"));
		//boardVO.setPageSize(propertiesService.getInt("pageSize"));

		/** pageing setting */
		/*
		 * PaginationInfo paginationInfo = new PaginationInfo();
		 * paginationInfo.setCurrentPageNo(boardVO.getPageIndex());
		 * paginationInfo.setRecordCountPerPage(boardVO.getPageUnit());
		 * paginationInfo.setPageSize(boardVO.getPageSize());
		 * 
		 * boardVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		 * boardVO.setLastIndex(paginationInfo.getLastRecordIndex());
		 * boardVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		 */

		List<?> list = boardService.selectBoardList(boardVO);
		model.addAttribute("resultList", list);

		int totCnt = boardService.selectBoardListTotCnt(boardVO);
		//paginationInfo.setTotalRecordCount(totCnt);
		//model.addAttribute("paginationInfo", paginationInfo);
		
		return "board/list";
	}
	
	@RequestMapping(value = "/mgmt.do")
	public String mgmt(ModelMap model) throws Exception {
		return "board/mgmt";
	}
	
	@RequestMapping(value = "/view.do")
	public String view(ModelMap model) throws Exception {
		return "board/view";
	}
	
	@RequestMapping(value = "/login.do")
	public String login(@RequestParam("user_id") String user_id, @RequestParam("password") String password,
			ModelMap model, HttpServletRequest request) throws Exception {
		
		//String aa = request.getParameter("user_id");
		//String bb = request.getParameter("password");
		
		System.out.println("user_id:"+user_id);
		System.out.println("password:"+password);
		
		BoardVO boardVO = new BoardVO();
		boardVO.setUserId(user_id);
		boardVO.setPassword(password);
		String user_name = boardService.selectLoginCheck(boardVO);
		
		if( user_name != null && !"".equals(user_name)) {
		request.getSession().setAttribute("userId", user_id);
		request.getSession().setAttribute("userName", user_name);
	}else {
		request.getSession().setAttribute("userId", "");
		request.getSession().setAttribute("userName", "");
		model.addAttribute("msg", "사용자 정보가 올바르지 않습니다.");
	}
		
		return "redirect:/list.do";
	}
	
	@RequestMapping(value = "/logout.do")
	public String logout(ModelMap model, HttpServletRequest request) throws Exception {
		
		request.getSession().invalidate();
		
		return "redirect:/list.do";
	}
	
}
