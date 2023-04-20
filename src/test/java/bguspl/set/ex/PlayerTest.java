package bguspl.set.ex;

import bguspl.set.Config;
import bguspl.set.Env;
import bguspl.set.UserInterface;
import bguspl.set.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.Random;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PlayerTest {

    Player player;
    @Mock
    Util util;
    @Mock
    private UserInterface ui;
    @Mock
    private Table table;
    @Mock
    private Dealer dealer;
    @Mock
    private Logger logger;

    void assertInvariants() {
        assertTrue(player.id >= 0);
        assertTrue(player.score() >= 0);
    }

    @BeforeEach
    void setUp() {
        // purposely do not find the configuration files (use defaults here).
        Env env = new Env(logger, new Config(logger, (String) null), ui, util);
        player = new Player(env, dealer, table, 0, false);
        assertInvariants();
    }

    @AfterEach
    void tearDown() {
        assertInvariants();
    }

    @Test
    void keyPressed(){
        ArrayBlockingQueue<Integer> setToCompare = new ArrayBlockingQueue<>(3);
        for (int i = 0; i < 3000; i++) {
            Random rand = new Random();
            int nextVal = rand.nextInt(12);
            boolean found = false;
            for(int j = 0; j<3 & !found;j++){
                found = setToCompare.contains(nextVal);
            }

            if(found)
                setToCompare.remove(nextVal);
            else
                if(setToCompare.size()<3)
                    setToCompare.add(nextVal);
            try {
                player.keyPressed(nextVal);
            } catch (Exception playerThreadInteruption) {
            }

            boolean foundInPlayer = false;
            Set a = player.getslotsToPress();
            for (int j = 0; j < 3 & !foundInPlayer; j++) {
                if(a.getSlot(j)==nextVal)
                    foundInPlayer = true;
            }
            try {
                if(found)
                    assertEquals(!found, foundInPlayer);
                else
                    assertEquals(found, foundInPlayer);

            } catch (Exception e) {
                System.out.println(setToCompare);
                System.out.println(a);
            }
        }
    }

    @Test
    void terminate(){
        try {
            player.terminate();
        } catch (Exception playerInteruppted) {
            //terminate functions interupt the player thread
        }
        assertEquals(true, player.getTerminate());
    }
}