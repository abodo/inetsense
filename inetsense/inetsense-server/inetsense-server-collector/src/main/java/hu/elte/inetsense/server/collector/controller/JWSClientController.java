package hu.elte.inetsense.server.collector.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import hu.elte.inetsense.common.service.configuration.ConfigurationNames;
import hu.elte.inetsense.server.service.configuration.ServerConfigurationProvider;

@Controller
@RequestMapping("/Inetsense-Client")
@EnableWebMvc
@ComponentScan(basePackageClasses = JWSClientController.class)
public class JWSClientController {

    @Autowired
    private ServerConfigurationProvider configurationProvider;

    @RequestMapping(method = RequestMethod.GET)
    public String showClient(Model model) {
        model.addAttribute("version", configurationProvider.getString(ConfigurationNames.INETSENSE_PROJECT_VERSION));
        model.addAttribute("built", configurationProvider.getString(ConfigurationNames.INETSENSE_PROJECT_BUILD_DATE));
        return "inetsense-client"; 
    }
    
    @RequestMapping(path = "/jnlp", method = RequestMethod.GET, produces = {"application/x-java-jnlp-file"})
    public String showJnlp(Model model, HttpServletRequest request, HttpServletResponse response) {
        buildResponse("inetsense.jnlp", model, request, response);
        return "inetsense-jnlp";
    }

    @RequestMapping(path = "/headless", method = RequestMethod.GET, produces = {"application/x-java-jnlp-file"})
    public String showHeadlessJnlp(Model model, HttpServletRequest request, HttpServletResponse response) {
        buildResponse("inetsense-headless.jnlp", model, request, response);
        return "inetsense-jnlp-headless";
    }

    protected void buildResponse(String filename, Model model, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/x-java-jnlp-file");
        response.setHeader("Content-Type", "application/x-java-jnlp-file");
        response.setHeader("Content-Disposition", "File Transfer");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        response.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
        response.setHeader("Pragma","no-cache");
        response.setDateHeader("Expires", 0);
        String codeBase = getCodeBase(request);
        model.addAttribute("codebase", codeBase);
    }

    protected String getCodeBase(HttpServletRequest request) {
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String codeBase = String.format("http://%s:%s", serverName, serverPort);
        return codeBase;
    }
}
