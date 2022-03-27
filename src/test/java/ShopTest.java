import flappyanimal.core.model.Character;
import flappyanimal.core.model.Player;
import flappyanimal.shop.BalanceToLowException;
import flappyanimal.shop.Shop;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ShopTest {

    private Shop cut; //code under test: Shop.java

    @Mock
    private Character charMock;

    @Mock
    private Player playerMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        cut = new Shop(Collections.singletonMap(1, charMock), playerMock);
    }

    @Test
    void testBuyCharacter() throws BalanceToLowException {
        ObservableList<Integer> unlockedChars = FXCollections.observableArrayList(1);

        when(playerMock.getBalance()).thenReturn(10);
        when(charMock.getCosts()).thenReturn(10);
        when(charMock.getId()).thenReturn(2);
        when(playerMock.getUnlockedChars()).thenReturn(unlockedChars);

        cut.buyCharacter(charMock);

        assertEquals(2, unlockedChars.size());
        assertEquals(1, unlockedChars.get(0));
        assertEquals(2, unlockedChars.get(1));

        verify(playerMock).setBalance(0);
        verify(playerMock).getUnlockedChars();
    }

    @Test
    void testBuyCharacterBalanceToLow() {
        ObservableList<Integer> unlockedChars = FXCollections.observableArrayList(1);

        when(playerMock.getBalance()).thenReturn(5);
        when(charMock.getCosts()).thenReturn(10);

        assertThrows(BalanceToLowException.class, () -> cut.buyCharacter(charMock));

        assertEquals(1, unlockedChars.size());
        assertEquals(1, unlockedChars.get(0));

        verify(playerMock, never()).setBalance(anyInt());
        verify(playerMock, never()).getUnlockedChars();
    }

    @Test
    void testSelectCharacter() {
        cut.selectCharacter(2);

        assertEquals(playerMock.getCurrentChar(), charMock.getId());

        verify(playerMock).setCurrentChar(2);
    }

    @Test
    void testGetPlayer() {
        Player act = cut.getPlayer();

        assertEquals(playerMock, act);
    }

}
