package fa.training.movietheater_mockproject.controller;

import fa.training.movietheater_mockproject.enums.AppConstant;
import fa.training.movietheater_mockproject.model.dto.MemberDto;
import fa.training.movietheater_mockproject.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping()
    String showMemberList(Model model, @RequestParam("size") Optional<Integer> sizeOpt,
                          @RequestParam("page") Optional<Integer> pageOpt,
                          @RequestParam("q") Optional<String> keywordOpt) {
        Integer size = sizeOpt.orElse(AppConstant.SIZE);
        Integer page = pageOpt.orElse(AppConstant.PAGE_NUMBER);
        String keyword = keywordOpt.orElse("");
        Integer offset = page * size;
        Integer totalMember = memberService.getTotalMember().get();
        Integer totalPage = 0;
        if (totalMember % size == 0) {
            totalPage = totalMember / size;
        } else {
            totalPage = totalMember / size + 1;
        }
        List<MemberDto> memberDtos = memberService.getMemberList(keyword, size, offset);
        model.addAttribute("memberDtos", memberDtos);

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        return "member/member-list";
    }
}
