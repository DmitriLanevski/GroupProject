package gameLogic.skills;

import gameLogic.buffs.Buff;

import java.util.ArrayList;
import java.util.List;

public class Skill {
    private List<Buff> buffs = new ArrayList<>();

    public synchronized List<Buff> getBuffs() {
        return buffs;
    }
}
