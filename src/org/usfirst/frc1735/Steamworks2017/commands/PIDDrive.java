// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc1735.Steamworks2017.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1735.Steamworks2017.Robot;
import org.usfirst.frc1735.Steamworks2017.subsystems.DriveTrain;

/**
 *
 */
public class PIDDrive extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
    private int m_mode;
    private double m_speed;
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
    // Manual variables
    // Get the initial (reference) gyro angle
    double m_initialGyroAngle;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public PIDDrive(int mode, double speed) {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        m_mode = mode;
        m_speed = speed;

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
     	// Assume for now we will never need to turn for more than a second.
    	//This protects us against a faulty gyro preventing us from believing we reached the desired angle
    	setTimeout(3);
    	
    	System.out.println("Issuing PID drive in mode " + m_mode + " at " + m_speed + " speed.");

    	m_initialGyroAngle = Robot.ahrs.getAngle();
    	System.out.println("Raw gyro value at init is" + m_initialGyroAngle);
    	
    	// Send the initial angle as the PID controller setpoint (i.e., maintain current heading while driving forward/backward)
    	Robot.driveTrain.drivelineController.setSetpoint(m_initialGyroAngle);
    	
    	// And enable the controller
    	Robot.driveTrain.drivelineController.enable();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
       	// Use mode bit to determine which driveline mode to use to accomplish the PID output reaction
    	if (DriveTrain.DrivetrainMode.fromInteger(m_mode) == DriveTrain.DrivetrainMode.kMecanum) {
    		Robot.driveTrain.mecanumDrive(0, m_speed, Robot.driveTrain.getPIDRotationRate()); // x,y,rot
    	}
    	else if (DriveTrain.DrivetrainMode.fromInteger(m_mode) == DriveTrain.DrivetrainMode.kTraction) {
    		Robot.driveTrain.arcadeDrive(m_speed, Robot.driveTrain.getPIDRotationRate());// move, rot
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.drivelineController.disable();
    	Robot.driveTrain.stop();
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    end();
    }
}
