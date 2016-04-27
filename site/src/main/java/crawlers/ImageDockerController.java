/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package crawlers;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dataBase.ConfigurationDatabase;
import dataBase.ImageDockerDatabase;
import errors.InternalError;
import models.Configuration;
import models.ImageDocker;
import ops.CommonOps;

/**
 * Controller for images. Manage every operation which deals with the projects.
 *
 * @author shathe
 */
@RestController
public class ImageDockerController {
    private static final Logger log = LoggerFactory.getLogger(ImageDockerController.class);
    @Autowired
    JdbcTemplate jdbcTemplate;
    CommonOps ops = new CommonOps();

    /**
     * Returns the images of a specified project
     */
    @RequestMapping(value = "/images", method = RequestMethod.GET)
    ResponseEntity<List<ImageDocker>> listImages(@RequestParam(value = "idProject") String idProject) {

        ImageDockerDatabase imageDB = new ImageDockerDatabase(jdbcTemplate);

        log.info("listImages " + idProject);
        List<ImageDocker> images;
        try {
            images = imageDB.getImages(idProject);
        } catch (Exception a) {
            throw new InternalError("Error listing images: " + a.getMessage());
        }

        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    /**
     * Returns the deleted image if it has been deleted, if not, returns an error message
     */
    @RequestMapping(value = "/deleteImage", method = RequestMethod.DELETE)
    ResponseEntity<ImageDocker> deleteImage(@RequestBody ImageDocker image) {
        List<ImageDocker> images = new ArrayList<>();
        images.add(image);
        try {
            ops.deleteImages(images, jdbcTemplate);
        } catch (Exception e) {
            log.warn("Error deleting: " + e.getMessage());
            throw new InternalError("Error deleting: " + e.getMessage());
        }

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    /**
     * Returns the updated image if it has been updated, if not, returns an error message
     */
    @RequestMapping(value = "/editImage", method = RequestMethod.POST)
    ResponseEntity<ImageDocker> editImage(@RequestBody ImageDocker image) {

        log.info("updating image " + image.getId());

        ImageDockerDatabase imageDB = new ImageDockerDatabase(jdbcTemplate);
        try {
            imageDB.updateImage(image);
            log.info("updated image " + image.getId());

        } catch (Exception a) {
            throw new InternalError("Error updating: " + a.getMessage());
        }

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    /**
     * Returns the created image if it has been created, if not, returns an error message
     */
    @RequestMapping(value = "/createImage", method = RequestMethod.POST)
    ResponseEntity<ImageDocker> createImage(@RequestParam(value = "idProject") String idProject,
                                            @RequestParam(value = "name") String name) {
        ConfigurationDatabase confDB = new ConfigurationDatabase(jdbcTemplate);
        // gets the last configuration of the project
        Configuration config = confDB.GetConfigurationFromProject(idProject);
        ImageDocker image = new ImageDocker(0, name, String.valueOf(config.getId()), idProject,
                new Date(System.currentTimeMillis()));
        ImageDockerDatabase imageDB = new ImageDockerDatabase(jdbcTemplate);
        imageDB.createImage(image);
        log.info("created image " + image.getName());
        image = imageDB.getImageJustCreated(idProject);

        String command = "java -jar ../butler.jar do build --imageName " + image.getId() + " --idProject "
                + idProject + "_" + config.getId();
        log.info("Command: " + command);
        ops.checkMessage(command, "successfully", "Not valid Image");

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

}
