package application.Services;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import application.SanctionsManager;

public class RefreshData {
	Timer timer;

    public RefreshData(int seconds) {
    	SanctionsManager start = new SanctionsManager();
        timer = new Timer();
		timer.schedule(new TimerTask() {
            @Override
            public void run() {
    			try {
    				start.UpdateSanctionedAdherents();
					start.AdherentExpiration();
				} catch (SQLException e) {
					e.printStackTrace();
				}
                System.out.println("Checking Sanctionned Adherents AND Checking Adherents Expiration : DONE!");
            }
        },0, seconds*1000);
	}

}
