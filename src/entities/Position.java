package entities;


// essentially a "tile", or a "box"
public class Position {

    private Agent agent;

    public Position() {
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        String toString = "";

        if (agent == null) toString = "- ";
        else {
            switch (agent.getStatus()) {
                case SICK -> toString = "S ";
                case IMMUNE -> toString = "I ";
                case DEAD -> toString = "D ";
                case VULNERABLE -> toString = "V ";
            }
        }

        return toString;
    }
}
