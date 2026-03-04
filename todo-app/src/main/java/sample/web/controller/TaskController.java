package sample.web.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sample.common.dao.entity.Task;
import sample.common.service.TaskService;
import sample.web.form.TaskForm;

@Controller
public class TaskController {

	private static final int PAGE_SIZE = 10;

	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	private String requireLogin(HttpSession session) {
		return (String) session.getAttribute("username");
	}

	private LocalDate parseDate(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		try {
			return LocalDate.parse(value);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	@GetMapping("/tasks")
	public String list(@RequestParam(value = "page", defaultValue = "1") int page,
			HttpSession session, Model model) {

		String username = requireLogin(session);
		if (username == null) {
			return "redirect:/login";
		}

		int total = taskService.countByUsername(username);

		// total=0 でも totalPages は 1 にしておく
		int totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

		int currentPage = Math.min(Math.max(page, 1), totalPages);
		int offset = (currentPage - 1) * PAGE_SIZE;

		List<Task> tasks = taskService.findByUsername(username, PAGE_SIZE, offset);

		model.addAttribute("tasks", tasks);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("total", total);
		model.addAttribute("username", username);

		return "tasks/list";
	}

	@GetMapping("/tasks/new")
	public String newForm(HttpSession session, Model model) {
		String username = requireLogin(session);
		if (username == null) {
			return "redirect:/login";
		}
		model.addAttribute("taskForm", new TaskForm());
		return "tasks/form-new";
	}

	@PostMapping("/tasks")
	public String create(@Valid TaskForm form, BindingResult result,
			HttpSession session) {
		String username = requireLogin(session);
		if (username == null) {
			return "redirect:/login";
		}
		if (result.hasErrors()) {
			return "tasks/form-new";
		}
		taskService.create(
				username,
				form.getTitle(),
				form.getContent(),
				form.getName(),
				parseDate(form.getStartDate()),
				parseDate(form.getEndDate()));
		return "redirect:/tasks";
	}

	@GetMapping("/tasks/edit/{id}")
	public String editForm(@PathVariable("id") Long id, HttpSession session, Model model) {
		String username = requireLogin(session);
		if (username == null) {
			return "redirect:/login";
		}
		Task task = taskService.findById(id);
		if (task == null || !username.equals(task.getUsername())) {
			return "redirect:/tasks";
		}

		TaskForm form = new TaskForm();
		form.setId(task.getId());
		form.setTitle(task.getTitle());
		form.setContent(task.getContent());
		form.setName(task.getName());
		form.setStartDate(task.getStartDate() == null ? "" : task.getStartDate().toString());
		form.setEndDate(task.getEndDate() == null ? "" : task.getEndDate().toString());

		model.addAttribute("taskForm", form);
		return "tasks/form-edit";
	}

	@PostMapping("/tasks/update/{id}")
	public String update(@PathVariable("id") Long id, @Valid TaskForm form,
			BindingResult result, HttpSession session) {
		String username = requireLogin(session);
		if (username == null) {
			return "redirect:/login";
		}
		if (result.hasErrors()) {
			form.setId(id);
			return "tasks/form-edit";
		}

		Task task = taskService.findById(id);
		if (task == null || !username.equals(task.getUsername())) {
			return "redirect:/tasks";
		}

		taskService.update(
				id,
				form.getTitle(),
				form.getContent(),
				form.getName(),
				parseDate(form.getStartDate()),
				parseDate(form.getEndDate()));

		return "redirect:/tasks";
	}

	@PostMapping("/tasks/delete/{id}")
	public String delete(@PathVariable("id") Long id, HttpSession session) {
		String username = requireLogin(session);
		if (username == null) {
			return "redirect:/login";
		}
		Task task = taskService.findById(id);
		if (task != null && username.equals(task.getUsername())) {
			taskService.delete(id);
		}
		return "redirect:/tasks";
	}
}