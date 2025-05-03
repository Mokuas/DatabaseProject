package test;

import main.services.BaseService;

import java.util.List;

/**
 * Class contains static methods for utilities used in test cases
 */
public class TestUtils {

    /**
     * removes all models in list from database
     * @param service service to call 'remove' method
     * @param list list of models
     * @param <Model> type of the model
     * @param <Service> type of the service
     */
    public static <Model, Service extends BaseService<Model>> void clearModels(Service service, List<Model> list) {
        for(Model model : list) {
            try {
                service.remove(model);
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}
