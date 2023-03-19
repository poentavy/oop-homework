package implementation;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Input;

public final class Main {

    private Main() { }

    /**
     * Implementation main
     * @param inputData input data
     * @param output output node
     */
    public static void main(final Input inputData, final ArrayNode output) {
        Game.getInstance().init(inputData, output);
        Game.getInstance().run();
    }
}
