package container;

import org.junit.Before;
import org.junit.Test;
import util.FileManager;

/**
 * Created by edwardlol on 17-4-17.
 */
public class containerDemos {
    private static ContainerStastics containerStastics = new ContainerStastics();
    @Before
    public void init() {
        containerStastics.init("datasets/original/thm_conta_ei");
    }

    @Test
    public void drawDemo() {

        /*
        disp_top(8);
        disp_data(10);

        int the_day = get_max_derivate();
        System.out.println("the day: " + the_day);

        get_conta_id_list(the_day);

        record_ids(FileManager.conta_static);

*/
        containerStastics.draw("./aaa.png");
    }

    @Test
    public void recordDemo() {
        containerStastics.recordDays("datasets/days", 0);
    }
}
