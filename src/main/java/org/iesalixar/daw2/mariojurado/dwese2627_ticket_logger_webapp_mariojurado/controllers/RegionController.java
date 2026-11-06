package org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.daos.RegionDAO;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.dtos.*;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.entities.Region;
import org.iesalixar.daw2.mariojurado.dwese2627_ticket_logger_webapp_mariojurado.mappers.RegionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/regions")
public class RegionController {
    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);
    @Autowired
    private RegionDAO regionDAO;
    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listRegions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "id") String sortField, @RequestParam(defaultValue = "asc") String sortDir, Model model) {
        page = Math.max(page, 0);
        size = Math.max(size, 1);
        long total = regionDAO.countRegions();
        int totalPages = (int) Math.ceil((double) total / size);
        if (totalPages > 0 && page >= totalPages) page = totalPages - 1;
        model.addAttribute("listRegions", RegionMapper.toDTOList(regionDAO.listRegionsPage(page, size, sortField, sortDir)));
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("offset", page * size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", total);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", "asc".equals(sortDir) ? "desc" : "asc");
        return "views/region/region-list";
    }

    @GetMapping("/detail")
    public String showDetail(@RequestParam Long id, Model model, RedirectAttributes ra, Locale locale) {
        try {
            Region e = regionDAO.getRegionById(id);
            if (e == null) {
                ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.region-controller.detail.notFound", null, locale));
                return "redirect:/regions";
            }
            model.addAttribute("region", RegionMapper.toDetailDTO(e));
            model.addAttribute("detailId", id);
            return "views/region/region-detail";
        } catch (Exception e) {
            logger.error("Error loading region detail {}", id, e);
            ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.region-controller.detail.error", null, locale));
            return "redirect:/regions";
        }
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("region", new RegionCreateDTO());
        return "views/region/region-form";
    }

    @PostMapping("/insert")
    public String insertRegion(@Valid @ModelAttribute("region") RegionCreateDTO dto, BindingResult result, RedirectAttributes ra, Locale locale) {
        if (result.hasErrors()) return "views/region/region-form";
        try {
            if (regionDAO.existsRegionByCode(dto.getCode())) {
                ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale));
                return "redirect:/regions/new";
            }
            regionDAO.insertRegion(RegionMapper.toEntity(dto));
        } catch (Exception e) {
            logger.error("Error inserting region", e);
            ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.region-controller.insert.error", null, locale));
        }
        return "redirect:/regions";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long id, Model model, RedirectAttributes ra, Locale locale) {
        Region e = regionDAO.getRegionById(id);
        if (e == null) {
            ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.region-controller.detail.notFound", null, locale));
            return "redirect:/regions";
        }
        model.addAttribute("region", RegionMapper.toUpdateDTO(e));
        return "views/region/region-form";
    }

    @PostMapping("/update")
    public String updateRegion(@Valid @ModelAttribute("region") RegionUpdateDTO dto, BindingResult result, RedirectAttributes ra, Locale locale) {
        if (result.hasErrors()) return "views/region/region-form";
        try {
            if (regionDAO.existsRegionByCodeAndNotId(dto.getCode(), dto.getId())) {
                ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.region-controller.update.codeExist", null, locale));
                return "redirect:/regions/edit?id=" + dto.getId();
            }
            Region existing = regionDAO.getRegionById(dto.getId());
            if (existing == null) return "redirect:/regions";
            RegionMapper.copyToExistingEntity(dto, existing);
            regionDAO.updateRegion(existing);
        } catch (Exception e) {
            logger.error("Error updating region {}", dto.getId(), e);
            ra.addFlashAttribute("errorMessage", messageSource.getMessage("msg.region-controller.update.error", null, locale));
        }
        return "redirect:/regions";
    }

    @PostMapping("/delete")
    public String deleteRegion(@RequestParam Long id, RedirectAttributes ra) {
        try {
            regionDAO.deleteRegion(id);
        } catch (Exception e) {
            logger.error("Error deleting region {}", id, e);
            ra.addFlashAttribute("errorMessage", "Error al eliminar la región.");
        }
        return "redirect:/regions";
    }
}
