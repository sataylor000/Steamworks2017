// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc1735.Steamworks2017.subsystems;

import org.usfirst.frc1735.Steamworks2017.RobotMap;
import org.usfirst.frc1735.Steamworks2017.commands.*;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class GearPresence extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final DigitalInput bannerSensor = RobotMap.gearPresenceBannerSensor;
    private final Solenoid gearPresenceRelay = RobotMap.gearPresenceGearPresenceRelay;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS


    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new MonitorGearPresence());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
    
    public void gearPresenceLightOn(boolean onState) {
    	// If onState is true, turn the camera light relay on.
    	// Otherwise, turn it off.
    	gearPresenceRelay.set(onState);
    	// Print the new state of the light to the SmartDashboard
		//SmartDashboard.putBoolean("Gear Presence Light On", onState);
    }
    
    public boolean isGearPresent() {
    	// Get the current state of the banner sensor.
    	// if it is TRUE, then the beam is broken and the gear is present.
    	// if it is FALSE, then the beam is unbroken (reflected back and received) and the gear is NOT present.
    	// This implementation is done so that if the banner sensor fails or gets disconnected, the pullup resistor on the RoboRio will make it look like the beam is still there (no gear, no light)
    	return (bannerSensor.get());
    }

}
