import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMoore {

    @Test
    public void test1() {
        String actual = Moore.mooreStr(1);
        String expected = "LFL+F+LFL";
        assertEquals(expected, actual);
    }

    @Test
    public void test2() {
        String actual = Moore.mooreStr(2);
        String expected = "−RF+LFL+FR−F−RF+LFL+FR−+F+−RF+LFL+FR−F−RF+LFL+FR−";
        assertEquals(expected, actual);
    }

    @Test
    public void test3() {
        String actual = Moore.mooreStr(3);
        String expected = "-+LF-RFR-FL+F+-RF+LFL+FR-F-RF+LFL+FR-+F+LF-RFR-FL+-F-+LF-RFR-FL+F+-RF+LFL+FR-F-RF+LFL+FR-+F+LF-RFR-FL+-+F+-+LF-RFR-FL+F+-RF+LFL+FR-F-RF+LFL+FR-+F+LF-RFR-FL+-F-+LF-RFR-FL+F+-RF+LFL+FR-F-RF+LFL+FR-+F+LF-RFR-FL+-";
        assertEquals(expected, actual);
    }

}
