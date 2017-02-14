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

import org.usfirst.frc1735.Steamworks2017.Robot;
import org.usfirst.frc1735.Steamworks2017.RobotMap;
import org.usfirst.frc1735.Steamworks2017.commands.*;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class Shooter extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

	// The shootFollower is configured via talon hardware to match the master.
	// We do not expect to actually access the follower after intial configuration.
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final CANTalon shootMaster = RobotMap.shooterShootMaster;
    private final CANTalon shootFollower = RobotMap.shooterShootFollower;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public void shooterInit() {
    	// Choose the sensor and sensor dirction
    	shootMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	shootMaster.reverseSensor(false); //Assume no inversion at this time.
    	shootMaster.configEncoderCodesPerRev(40); // Spec says 20 pulses per channel per rev
    	shootMaster.configNominalOutputVoltage(+0f,  -0f);
    	shootMaster.configPeakOutputVoltage(+12f,  -0f); // Config to NEVER run in reverse!
    	
    	// @FIXME:  We might need to add a voltage ramp rate if we are trying to spin up too fast and drawing too much current.
    	//shootMaster.setCloseLoopRampRate(12); // in units of volts in one second, this would let the motor take one second to go to full throttle.
    	// or might be .setVoltageRampRate()
    	
    	/* set the allowable closed-loop error,
         * Closed-Loop output will be neutral within this range.
         * See Table in Section 17.2.1 for native units per rotation. 
         */
    	// I THINK this means stop adjusting and just maintain feed-forward?
    	// Seems like 1% of 3600 RPM would be fine (36 * 4 = 144)
        shootMaster.setAllowableClosedLoopErr(144);// in raw units of 4xCPR (4x fidelity of codes per rev)
        /* set closed loop gains in slot0 */
        shootMaster.setProfile(0);
        shootMaster.setF(0.1097);
        shootMaster.setP(0.22);
        shootMaster.setI(0.0); 
        shootMaster.setD(0.0);
         
        // Set the mode to be velocity-based
        shootMaster.changeControlMode(TalonControlMode.Speed);
        // Must set an initial setpoint as well, in RPMs
        shootMaster.set(0); // start with shooter off!
        
        //Put an initial value for setpoint RPM to the SmartDashboard
        SmartDashboard.putNumber("ShooterRPM",  0);
	}    	

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
    
    // Setpoint is in RPM
    public void operate(double setpoint) {
    	// Given a setpoint, fire up the shooter.  Let the caller decide how fast.
    	shootMaster.set(setpoint);
    	
    	// Print our requested speed, error, and motor output
    	if (Robot.isDbgOn()) {
    		// Onlly do these calulations if debugging
    		double motorOutput = shootMaster.getOutputVoltage() / shootMaster.getBusVoltage();
    		m_sb.append("Shooter:  targetSpeed = ");
    		m_sb.append(setpoint);
    		m_sb.append("\tError = ");
    		m_sb.append(shootMaster.getClosedLoopError());
    		m_sb.append("\tMotorOutput = ");
    		m_sb.append(motorOutput);
    		
    		if ((++m_loops%10) == 0) {
    			System.out.print(m_sb.toString());
    		}
    	}
    }
  
    
    // Member variables
    int m_loops = 0; // for reducing frequency of print statements
    StringBuilder m_sb = new StringBuilder(); // class for building up strings across multiple code lines
    
}

