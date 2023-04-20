package bguspl.set.ex;

import bguspl.set.Config;
import bguspl.set.Env;
import bguspl.set.UserInterface;
import bguspl.set.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ArrayBlockingQueue;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DealerTest {

    Dealer dealer;
    @Mock
    Util util;
    @Mock
    private UserInterface ui;
    @Mock
    private Table table;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        // purposely do not find the configuration files (use defaults here).
        Env env = new Env(logger, new Config(logger, (String) null), ui, util);
        player1 = new Player(env, dealer, table, 1, true);
        player2 = new Player(env, dealer, table, 2, true);
        player3 = new Player(env, dealer, table, 3, true);
        player4 = new Player(env, dealer, table, 4, true);
        Player[] arr = {player1,player2,player3,player4};
        dealer = new Dealer(env, table, arr);
    }

    @Test
    void unblockPlayers(){
        dealer.unblockPlayers();
        assertEquals(true, player1.tryToAccessTable());
        assertEquals(true, player2.tryToAccessTable());
        assertEquals(true, player3.tryToAccessTable());
        assertEquals(true, player4.tryToAccessTable());
    }

    @Test
    void addSetToCheck(){
        Set[] tmparr = new Set[5];
        for (int i = 0; i < 4; i++) {
            tmparr[i] = new Set(i);
            try {
                dealer.addSetToCheck(tmparr[i]);
            } catch (Exception dealerThreadInterupted) {
                System.out.println("set no." + i + " has been added successfully");
            }
        }
        ArrayBlockingQueue<Set> tmp =  dealer.getsetsToCheck();
        for (int i = 0; i < 4; i++) {
            assertEquals(true, tmp.contains(tmparr[i]));
        }
        try {
            tmparr[4] = new Set(4);
            dealer.addSetToCheck(tmparr[4]);
        } catch (Exception success) {
            assertEquals(false, tmp.contains(tmparr[4]));
            System.out.println("set no." + 4 + " has not been added successfully");
        }
    }
}
