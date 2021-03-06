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

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc1735.Steamworks2017.subsystems.*;

/**
 *
 */
public class AutonomousHopperAndShoot extends CommandGroup {


    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
    public AutonomousHopperAndShoot() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS
 
    // Drive forward until we are even with the hopper kickbar
    //addSequential(new Delay(0.5));
	
    addParallel(new TurretWithLimits(-0.5));
    addSequential(new DriveWithProgram(DriveTrain.DrivetrainMode.kTraction,
			3, // timeout
			0.5,85.0, // drive MagDir, dist
			0,0, // crab MagDir, dist
			0)); // Angle to turn
	//addSequential(new Delay(0.5));

	// Crab over to the kickplate and hit it
	addSequential(new DriveWithProgram(DriveTrain.DrivetrainMode.kMecanum,
			4, // timeout
			0,0, // drive MagDir, dist
			1.0,59, // crab MagDir, dist
			0)); // Angle to turn
	
	//addSequential(new Delay(0.5));

	// Spin up the shooter
	addParallel(new ShooterStart());
	// In parallel, scoot a bit closer to the boiler so that we are lined up with the hopper opening?
	addSequential(new DriveWithProgram(DriveTrain.DrivetrainMode.kMecanum,
			1, // timeout
			-0.5,3.5, // drive MagDir, dist
			0,0, // crab MagDir, dist
			0)); // Angle to turn
	
	// possibly scoot closer to the hopper if needed
	//addSequential(new DriveWithProgram(DriveTrain.DrivetrainMode.kMecanum,
	//		.5, // timeout
	//		0,0, // drive MagDir, dist
	//		.5,0, // crab MagDir, dist
	//		0)); // Angle to turn
	
	
	addSequential(new Delay(1.75));
	addSequential(new FeederStart(1.0), 10); // Run the feeder for seven seconds.  Tune this to terminate near the end of autonomous period.  Stops automatically.
	addSequential(new ShooterStop()); // Kill the shooter motors.  They will coast for a while.
	
	
    } 
}
