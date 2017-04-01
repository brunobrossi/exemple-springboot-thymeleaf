
package com.algaworks.cobranca.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.cobranca.model.StatusTitulo;
import com.algaworks.cobranca.model.Titulo;
import com.algaworks.cobranca.repository.filter.TituloFilter;
import com.algaworks.cobranca.service.CadastroTituloService;

@Controller
@RequestMapping("/titulos")
public class TituloController {

	private static final String CADASTRO_VIEW = "CadastroTitulo";

	@Autowired
	private CadastroTituloService cadastroTituloService;

	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(new Titulo());
		return mv;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			return CADASTRO_VIEW;
		}

		try {
			cadastroTituloService.save(titulo);
			redirectAttributes.addFlashAttribute("mensagem", "Título salvo com sucesso!");
			return "redirect:/titulos/novo";
		} catch (IllegalArgumentException e) {
			errors.rejectValue("dataVencimento", null, e.getMessage());
			return CADASTRO_VIEW;
		}
	}

	@RequestMapping
	public ModelAndView consultaTitulos(@ModelAttribute("filtro") TituloFilter filtro) {
		List<Titulo> titulosFiltrados = cadastroTituloService.findByDescricaoContaining(filtro);
		ModelAndView mv = new ModelAndView("ConsultaTitulos");
		mv.addObject("titulos", titulosFiltrados);
		return mv;
	}

	@RequestMapping("{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Titulo titulo) {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(titulo);
		return mv;
	}

	@RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long codigo) {

		return cadastroTituloService.receber(codigo);

	}

	@RequestMapping(value = "{codigo}", method = RequestMethod.DELETE)
	public String delete(@PathVariable Long codigo, RedirectAttributes attributes) {
		attributes.addFlashAttribute("mensagem", "Título excluido com sucesso!");
		cadastroTituloService.delete(codigo);
		return "redirect:/titulos";

	}

	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> todosStatusTitulo() {
		return Arrays.asList(StatusTitulo.values());
	}

}
