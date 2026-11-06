package org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.daos.*;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.dtos.*;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.entities.*;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.mappers.ProvinceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/provinces")
public class ProvinceController {
    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ProvinceDAO provinceDAO;
    @Autowired
    private RegionDAO regionDAO;

    @GetMapping
    public String listProvinces(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String sortField, @RequestParam(defaultValue = "asc") String sortDir, Model model) {
        page = Math.max(page, 0);
        size = Math.max(size, 1);
        long total = provinceDAO.countProvinces();
        int totalPages = (int) Math.ceil((double) total / size);
        if (totalPages > 0 && page >= totalPages) page = totalPages - 1;
        model.addAttribute("listProvinces", ProvinceMapper.toDTOList(provinceDAO.listProvincesPage(page, size, sortField, sortDir)));
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("offset", page * size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", total);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", "asc".equals(sortDir) ? "desc" : "asc");
        return "views/province/province-list";
    }

    @GetMapping("/detail")
    public String showDetail(@RequestParam Long id, Model model, RedirectAttributes ra, Locale locale) {
        try {
            Province p = provinceDAO.getProvinceById(id);
            if (p == null) {
                ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.province-controller.detail.notFound", null, locale));
                return "redirect:/provinces";
            }
            model.addAttribute("province", ProvinceMapper.toDetailDTO(p));
            model.addAttribute("detailId", id);
            return "views/province/province-detail";
        } catch (Exception e) {
            logger.error("Error loading province detail {}", id, e);
            ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.province-controller.detail.error", null, locale));
            return "redirect:/provinces";
        }
    }

    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {
        model.addAttribute("province", new ProvinceCreateDTO());
        return addRegions(model, locale, "views/province/province-form");
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long id, Model model, RedirectAttributes ra, Locale locale) {
        Province p = provinceDAO.getProvinceById(id);
        if (p == null) {
            ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.province-controller.edit.notfound", null, locale));
            return "redirect:/provinces";
        }
        model.addAttribute("province", ProvinceMapper.toUpdateDTO(p));
        return addRegions(model, locale, "views/province/province-form");
    }

    @PostMapping("/insert")
    public String insertProvince(@Valid @ModelAttribute("province") ProvinceCreateDTO dto, BindingResult result, Model model, RedirectAttributes ra, Locale locale) {
        if (result.hasErrors()) return addRegions(model, locale, "views/province/province-form");
        try {
            if (provinceDAO.existsProvinceByCode(dto.getCode())) {
                ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.province-controller.insert.codeExist", null, locale));
                return "redirect:/provinces/new";
            }
            provinceDAO.insertProvince(ProvinceMapper.toEntity(dto));
        } catch (Exception e) {
            logger.error("Error inserting province", e);
            ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.province-controller.insert.error", null, locale));
        }
        return "redirect:/provinces";
    }

    @PostMapping("/update")
    public String updateProvince(@Valid @ModelAttribute("province") ProvinceUpdateDTO dto, BindingResult result, Model model, RedirectAttributes ra, Locale locale) {
        if (result.hasErrors()) return addRegions(model, locale, "views/province/province-form");
        try {
            if (provinceDAO.existsProvinceByCodeAndNotId(dto.getCode(), dto.getId())) {
                ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.province-controller.update.codeExist", null, locale));
                return "redirect:/provinces/edit?id=" + dto.getId();
            }
            Province existing = provinceDAO.getProvinceById(dto.getId());
            if (existing == null) return "redirect:/provinces";
            ProvinceMapper.copyToExistingEntity(dto, existing);
            provinceDAO.updateProvince(existing);
        } catch (Exception e) {
            logger.error("Error updating province {}", dto.getId(), e);
            ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.province-controller.update.error", null, locale));
        }
        return "redirect:/provinces";
    }

    @PostMapping("/delete")
    public String deleteProvince(@RequestParam Long id, RedirectAttributes ra, Locale locale) {
        try {
            provinceDAO.deleteProvince(id);
        } catch (Exception e) {
            logger.error("Error deleting province {}", id, e);
            ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.province-controller.delete.error", null, locale));
        }
        return "redirect:/provinces";
    }

    private String addRegions(Model model, Locale locale, String view) {
        try {
            List<Region> regions = regionDAO.listAllRegions();
            model.addAttribute("listRegions", regions);
        } catch (Exception e) {
            logger.error("Error loading regions for province form", e);
            model.addAttribute("errorMessage", messageSource.getMessage("msg.province-controller.edit.error", null, locale));
        }
        return view;
    }
}
