/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;


/**
 * IdeTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-03 nsano initial version <br>
 */
@EnabledIfSystemProperty(named = "vavi.test", matches = "ide")
class IdeTest {

    @Test
    void test_simple() throws Exception {
        com.martijncourteaux.multitouchgestures.demo.DemoSimpleGestures.main(new String[0]);
    }

    @Test
    void test_compare() throws Exception {
        com.martijncourteaux.multitouchgestures.demo.DemoCompareGestures.main(new String[0]);
    }

    @Test
    void test_apple() throws Exception {
        com.martijncourteaux.multitouchgestures.demo.DemoAppleGestures.main(new String[0]);
    }

    @AfterEach
    void teardown() {
        while (true)
            Thread.yield();
    }
}

/* */
