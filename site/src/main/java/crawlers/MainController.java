/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package crawlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This controller gives the html files when they are requested
 * @author shathe
 *
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/nosotros")
    public String nosotros() {
        return "nosotros";
    }
    
    @RequestMapping("/home")
    public String home() {
        return "home";
    }
    
    @RequestMapping("/imagesPage")
    public String images() {
        return "imagesPage";
    }
    @RequestMapping("/containersPage")
    public String containers() {
        return "containersPage";
    }    
    @RequestMapping("/containerItem")
    public String containerItem() {
        return "containerItem";
    }    
    
    @RequestMapping("/contacto")
    public String contacto() {
        return "contacto";
    }    
    
    @RequestMapping("/perfil")
    public String perfil() {
        return "perfil";
    }
    
    @RequestMapping("/crawlers")
    public String crawlers(){
        return "crawlers";
    }

}
