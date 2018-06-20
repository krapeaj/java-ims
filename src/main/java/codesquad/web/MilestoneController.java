package codesquad.web;

import codesquad.domain.Milestone;
import codesquad.domain.User;
import codesquad.dto.MilestoneDto;
import codesquad.security.LoginUser;
import codesquad.service.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/milestones")
public class MilestoneController {

    @Autowired
    private MilestoneService milestoneService;

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("milestones", milestoneService.findAll());
        return "/milestone/list";
    }

    @GetMapping("/form")
    public String form(@LoginUser User loginUser) {
        return "/milestone/form";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser, MilestoneDto milestoneDto) {
        milestoneService.add(milestoneDto);
        return "redirect:/milestones";
    }
}
