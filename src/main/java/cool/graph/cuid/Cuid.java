package cool.graph.cuid;

import android.content.Context;
import android.provider.Settings;

import java.math.BigInteger;
import java.util.Date;

/**
 * Generates collision-resistant unique ids.
 */
public class Cuid {
    private static final int BASE = 36;
    private static final int BLOCK_SIZE = 4;
    private static final int DISCRETE_VALUES = (int) Math.pow(BASE, BLOCK_SIZE);
    private static final String LETTER = "c";

    private Context context;

    public Cuid(Context context) {
        this.context = context;
    }

    private static int counter = 0;

    private String getAndroidId() {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    private String getFingerprint() {
        String androidId = getAndroidId();

        int acc = androidId.length() + BASE;
        for (int i = 0; i < androidId.length(); i++) {
            acc += acc + (int) androidId.charAt(i);
        }

        String idBlock = pad(new BigInteger(androidId, 16).toString(BASE), 2);
        String nameBlock = pad(Integer.toString(acc), 2);

        return idBlock + nameBlock;
    }

    private String pad(String input, int size) {
        // courtesy of http://stackoverflow.com/a/4903603/1176596
        String repeatedZero = new String(new char[size]).replace("\0", "0");
        String padded = repeatedZero + input;
        return (padded).substring(padded.length() - size);
    }

    private String getRandomBlock() {
        return pad(Integer.toString((int) (Math.random() * DISCRETE_VALUES), BASE), BLOCK_SIZE);
    }

    private int safeCounter() {
        counter = counter < DISCRETE_VALUES ? counter : 0;
        return counter++;
    }

    /**
     * Generates collision-resistant unique ids.
     *
     * @return a collision-resistant unique id
     */
    public String createCuid() {
        String timestamp = Long.toString(new Date().getTime(), BASE);
        String counter = pad(Integer.toString(safeCounter(), BASE), BLOCK_SIZE);
        String random = getRandomBlock() + getRandomBlock();

        return LETTER + timestamp + counter + getFingerprint() + random;
    }
}
