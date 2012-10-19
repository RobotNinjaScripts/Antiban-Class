import java.awt.Point;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/**
 * 
 * @author JackOff
 *
 */

public class Antiban {
	
	public static void rotateScreen() {
		
		if(Players.getLocal().getInteracting() != null) {
			
			Camera.turnTo(Players.getLocal().getInteracting()); // Turns camera to the object you're interacting with
			Camera.setPitch(Camera.getPitch() + Random.nextInt(-10, 10)); // Rotate up or down a random amount.
			Camera.setAngle(Camera.getYaw() + Random.nextInt(-22, 22)); // Rotate left or right a random amount.
			
		} else {
			
			// Since you're not interacting with anything, allow a larger rotation.
			Camera.setPitch(Camera.getPitch() + Random.nextInt(-40, 40)); // Rotate up or down a random amount.
			Camera.setAngle(Camera.getYaw() + Random.nextInt(-83, 83)); // Rotate left or right a random amount.
			
		}
		
	}
	
	public static void checkSkill(int index) {
		
		Tabs currentTab = Tabs.getCurrent();
		
		// This could be used as Antiban.checkSkill(Random.nextInt(0, 24))
		Tabs.STATS.open(); // Self explanatory - opens STATS tab.
		Skills.getWidgetChild(index).hover(); // Hovers the specified stat
		
		Task.sleep(500);
		
		currentTab.open(); // Go back to previous tab
		
	}
	
	public static void checkPlayer() {
		
		Player[] players = Players.getLoaded(); // Gets all players around
		int whatPlayer = Random.nextInt(0, players.length); // Which player to hover
		
		if(Players.getLocal().getInteracting() == null) {
			Camera.turnTo(players[whatPlayer]); //If you're not doing anything, turn to the player
		}
		
		players[whatPlayer].hover(); // Hovers the aforementioned random player
		
	}
	
	public void checkTab(Tabs tab) {
		
		Tabs currentTab = Tabs.getCurrent();
		
		tab.open(); // Open specified tab
		
		Task.sleep(500);
		
		currentTab.open(); // Go back to previous tab
		
	}
	
	public static void random() {
		
		int randomAnti = Random.nextInt(1, 4);
		
		switch(randomAnti) {
		
			case 1:
				rotateScreen();
			break;
			
			case 2:
				checkSkill(Random.nextInt(0,24));
			break;
			
			case 3:
				checkPlayer();
			break;
			
			case 4:
				Tabs[] possibleTabs = { Tabs.CLAN_CHAT, Tabs.FRIENDS, Tabs.FRIENDS_CHAT }; // Tabs a human might check
				Tabs currentTab = Tabs.getCurrent();
				possibleTabs[Random.nextInt(0, 2)].open(); // Check random tab
				Task.sleep(500);
				currentTab.open(); // Return to previous tab
			break;
		
		}
		
	}
	
	public static boolean moveMouse(WidgetChild w) {
		
		int x = w.getAbsoluteX() + Random.nextInt(0, w.getWidth()), y = w.getAbsoluteY() + Random.nextInt(0, w.getHeight());
		
		int startX = Mouse.getX(),
			startY = Mouse.getY(),
			startDX = Mouse.getX(),
			startDY = Mouse.getY(); // where mouse started
		double distX = Math.abs( (double) startX - x ),
				distY = Math.abs( (double) startY - y ),
				dist = Math.sqrt( distX * distX + distY * distY ); // Distance from start to finish
		
		double width = 2.0 *  dist* ( (0.5 * (double) w.getWidth()) / Math.max(distX, distY));
		
		double time = Random.nextDouble(125, 150) + Random.nextDouble(90, 100) * (Math.log( 2.0 * dist / width ) / Math.log(2)); // fitts law brah
		
		double bPoint = 0;
		if(dist > 100) bPoint = dist / 20; // Last 20% of the distance
		
		double friction = Random.nextDouble(-1.00, 1.00); //Random friction, to be safe
		
		for(int i = 0; i < (int) dist; i++) {
			if(startDX < x)
				startDX++;
			else if (startDX > x) startDX--;
			
			if(startDY < y)
				startDY++;
			else if (startDY > y) startDY--;
			
			//TODO: save points, randomize using friction, find different friction for diff surfaces
		}
		
		if(Mouse.getX() == x && Mouse.getY() == y) return true;
		return false;
	}
	
}
