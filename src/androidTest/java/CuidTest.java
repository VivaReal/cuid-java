import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import cool.graph.cuid.Cuid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CuidTest {

    private Context context;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void createCuid() throws Exception {
        assertEquals(new Cuid(context).createCuid().charAt(0), 'c');
    }

    @Test
    public void counterIncreases() {
        assertTrue(new Cuid(context).createCuid().substring(12, 16).compareTo(new Cuid(context).createCuid().substring(12, 16)) < 0);
    }

    @Test
    public void fingerprintRemainsTheSame() {
        assertEquals(new Cuid(context).createCuid().substring(13, 17), new Cuid(context).createCuid().substring(13, 17));
    }

    @Test
    public void timestampChanges() throws InterruptedException {
        String timestamp1 = new Cuid(context).createCuid().substring(1, 9);
        Thread.sleep(10);
        String timestamp2 = new Cuid(context).createCuid().substring(1, 9);
        assertNotEquals(timestamp1, timestamp2);
    }

    private boolean hasNoCollisions() {
        int iterations = 1000;
        Map<String, String> cuids = new HashMap<>();

        for (int i = 0; i < iterations; ++i) {
            String cuid = new Cuid(context).createCuid();
            if (cuids.containsKey(cuid)) {
                return false;
            } else {
                cuids.put(cuid, cuid);
            }
        }

        return true;
    }

    @Test
    public void testForCollisions() {
        assertEquals(hasNoCollisions(), true);
    }
}