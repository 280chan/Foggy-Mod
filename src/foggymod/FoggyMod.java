package foggymod;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

import basemod.BaseMod;
import basemod.interfaces.PreUpdateSubscriber;

/**
 * @author 彼君不触
 * @version 4/27/2020
 * @since 7/27/2018
 */

@SpireInitializer
public class FoggyMod implements PreUpdateSubscriber {
	public static boolean changed = false;
	public static ArrayList<ArrayList<MapRoomNode>> map = null;
	
	public static void initialize() {
		new FoggyMod();
	}

	public FoggyMod() {
		BaseMod.subscribe(this);
	}

	@Override
	public void receivePreUpdate() {
		if (AbstractDungeon.player == null)
			return;
		if (map != AbstractDungeon.map) {
			if (AbstractDungeon.map != null) {
				map = AbstractDungeon.map;
			} else {
				AbstractDungeon.map = map;
			}
			changed = false;
		}
		if (!changed && map != null) {
			changed = true;
			int rowNum = 0;
			for (ArrayList<MapRoomNode> row : map) {
				boolean next = false;
				if (rowNum == 0)
					next = true;
				rowNum++;
				if (next)
					continue;
				for (MapRoomNode node : row)
					if (node.room == null || node.room.getClass().isAssignableFrom(MonsterRoom.class) || node.room instanceof EventRoom)
						node.room = new EventRoom();
			}
		}
		MapRoomNode n = AbstractDungeon.currMapNode;
		if (n != null && n.room != null && n.room instanceof EventRoom) {
			AbstractEvent e = n.room.event;
			if (e == null) {
				n.room.onPlayerEntry();
			}
		}
	}
	
}
