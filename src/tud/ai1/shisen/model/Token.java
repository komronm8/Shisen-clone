package tud.ai1.shisen.model;

import org.newdawn.slick.geom.Vector2f;
import tud.ai1.shisen.util.TokenDisplayValueProvider;

public class Token implements IToken {

    private static int counter = 0;
    private final int id;
    private TokenState state;
    private final int value;
    private Vector2f pos;

    public Token(TokenState state, int value, Vector2f pos) {
        this.id = counter;
        counter++;
        this.state = state;
        this.value = value;
        this.pos = pos;
    }

    public Token(int value) {
        this.value = value;
        this.state = TokenState.DEFAULT;
        this.pos = new Vector2f(0, 0);
        this.id = counter;
        counter++;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public TokenState getTokenState() {
        return this.state;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public Vector2f getPos() {
        return this.pos;
    }

    @Override
    public String getDisplayValue() {
        TokenDisplayValueProvider provider = TokenDisplayValueProvider.getInstance();
        return provider.getDisplayValue(this.value);
    }

    @Override
    public void setTokenState(TokenState abc) {
        this.state = abc;
    }

    @Override
    public void setPos(Vector2f pos) {
        this.pos = pos;
    }
}
