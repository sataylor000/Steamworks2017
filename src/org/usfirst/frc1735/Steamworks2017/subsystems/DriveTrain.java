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
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class DriveTrain extends Subsystem implements PIDOutput {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final CANTalon fLMotor = RobotMap.driveTrainFLMotor;
    private final CANTalon fRMotor = RobotMap.driveTrainFRMotor;
    private final CANTalon bLMotor = RobotMap.driveTrainBLMotor;
    private final CANTalon bRMotor = RobotMap.driveTrainBRMotor;
    private final RobotDrive robotDrive41 = RobotMap.driveTrainRobotDrive41;
    private final Compressor compressor = RobotMap.driveTrainCompressor;
    private final Solenoid solenoid = RobotMap.driveTrainSolenoid;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS


    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new DriveWIthJoysticks());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
    
    // Override constructor to initialize variables to defaults
    public DriveTrain() {
    	this.setTractionMode(); // Default to traction mode on startup.
    	m_isCrabExcursion = false; // We do not start out in any funny hybrid modes

    }    
        
    public enum DrivetrainMode {
        kMecanum(0), kTraction(1), kCrabExcursion(2);

    	@SuppressWarnings("MemberName")
    	public final int value;

    	private DrivetrainMode(int value) {
    		this.value = value;
      }
    	
    	// Provide a pseudo-cast operation so ints can be compared to enums
    	public static DrivetrainMode fromInteger(int mode) {
    		switch (mode) {
    		case 0:
    			return kMecanum;
    		case 1:
    			return kTraction;
    		case 2:
    			return kCrabExcursion;
    		}
    		return null;
    	}
    }
    
    public void drivetrainInit() {
    	// Do initialization that cannot be done in the constructor because robot.init isn't executed yet so we don't have a gyro instance.
    	
    	//-----------------------------
    	// Software PID Subsystem Initialization
    	//-----------------------------
    	//PID values.values  F is feed-forward for maintaining rotational velocity.
    	// The last two args are input source and output object (our PIDwrite() function)
        drivelineController = new PIDController(kP, kI, kD, kF, Robot.ahrs, this);
        drivelineController.setInputRange(-180.0f, 180.0f); // WHY IS THIS -180 to +180 RATHER THAN 0-360????
        drivelineController.setOutputRange(-1.0, 1.0); // What is the allowable range of values to send to the output (our motor rotation)
        drivelineController.setAbsoluteTolerance(kToleranceDegrees); // How close do we have to be in order to say we have reached the target?
        // Robot can spin in full circle so angle might wrap from 0 to 360.
        // 'Continuous' allows us to follow that wrap to get from 359 to 0, rather than going counterclockwise a full circle to get there.
        drivelineController.setContinuous(true);        
        /* Add the PID Controller to the Test-mode dashboard, allowing manual  */
        /* tuning of the Turn Controller's P, I and D coefficients.            */
        /* Typically, only the P value needs to be modified.                   */
        LiveWindow.addActuator("DriveSystem", "RotateController", drivelineController);
        
        //Initialize the motor controller (And encoder) hardware
    	// Choose the sensor and sensor dirction
    	fLMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    	fLMotor.reverseSensor(true); //Assume inversion to match drivetrain power inversion on left side
    	fRMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    	fRMotor.reverseSensor(false); //Assume no inversion at this time.
    	bLMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    	bLMotor.reverseSensor(true); //Assume inversion to match drivetrain power inversion on left side
    	bRMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    	bRMotor.reverseSensor(false); //Assume no inversion at this time.

    	// We might choose to use the controller PID for doing clean positional driving (e.g., drive 36 inches).  here's some initial setup thoughts
    	// Important note:  Changing the mode to .Position causes the motor controller to only obey setpoint commands.
    	// This renders the motor controller useless for our implementation of the joystick controls, which set the motor speed ia the WPILib routines.
    	// Thus, this code can't be used unless we ditch the software PID and use the full "Motion Magic" feature of the Talons...
    	if (false) {
	    	// No configEncoderCodesPerRev is needed for CTRE Mag Encoder.
	    	fLMotor.configNominalOutputVoltage(+0f,  -0f);
	    	fLMotor.configPeakOutputVoltage(+12f,  -12f);
	    	
	    	// @FIXME:  We might need to add a voltage ramp rate if we accellerate too quickly
	    	//fLMotor.setVoltageRampRate(something)
	    	
	    	/* set the allowable closed-loop error,
	         * Closed-Loop output will be neutral within this range.
	         * See Table in Section 17.2.1 for native units per rotation. 
	         */
	    	// Don't think we need this
	        //fLMotor.setAllowableClosedLoopErr(144);// in raw units of 4xCPR (4x fidelity of codes per rev)
	        /* set closed loop gains in slot0 */
	        fLMotor.setProfile(0);
	        fLMotor.setF(0);
	        fLMotor.setP(0.1);
	        fLMotor.setI(0.0); 
	        fLMotor.setD(0.0);

	        fRMotor.setProfile(0);
	        fRMotor.setF(0);
	        fRMotor.setP(0.1);
	        fRMotor.setI(0.0); 
	        fRMotor.setD(0.0);

	        bLMotor.setProfile(0);
	        bLMotor.setF(0);
	        bLMotor.setP(0.1);
	        bLMotor.setI(0.0); 
	        bLMotor.setD(0.0);

	        bRMotor.setProfile(0);
	        bRMotor.setF(0);
	        bRMotor.setP(0.1);
	        bRMotor.setI(0.0); 
	        bRMotor.setD(0.0);
	        
	        // Set the mode to be position-based
	        fLMotor.changeControlMode(TalonControlMode.Position);
	        fRMotor.changeControlMode(TalonControlMode.Position);
	        bLMotor.changeControlMode(TalonControlMode.Position);
	        bRMotor.changeControlMode(TalonControlMode.Position);
	        
	        // Must set an initial setpoint as well.  setpoint appears to be number of rotations desired (positive or negative)
	        fLMotor.set(0);
	        fRMotor.set(0);
	        bLMotor.set(0);
	        bRMotor.set(0);
    	}
    	
    	// We do want to clear the sensor values at the beginning, just to make debug easier.
    	RobotMap.driveTrainFLMotor.setEncPosition(0);
    	RobotMap.driveTrainFRMotor.setEncPosition(0);
    	RobotMap.driveTrainBLMotor.setEncPosition(0);
    	RobotMap.driveTrainBRMotor.setEncPosition(0);


    }

    public void arcadeDrive(double moveValue,double rotateValue) {
    	boolean squaredInputs = false; // Do not use decreased sensitivity at low speeds.
    	// Make sure we are in Traction mode:
    	this.setTractionMode();
    	robotDrive41.arcadeDrive(-moveValue, rotateValue, squaredInputs); //Asssume joystick inputs (Y fwd == -1)
    	printMXPInfo();
    	
    	if (Robot.isDbgOn()) {
    		// Print the encoder values
    		double FLCurrentRotation = -RobotMap.driveTrainFLMotor.getEncPosition()/2048.0; // Returns 4*1024 = 4096 units per rev
    		double FRCurrentRotation = RobotMap.driveTrainFRMotor.getEncPosition()/2048.0;
    		double BLCurrentRotation = -RobotMap.driveTrainBLMotor.getEncPosition()/2048.0;
    		double BRCurrentRotation = RobotMap.driveTrainBRMotor.getEncPosition()/2048.0;
    		
    		System.out.println("FL = " + FLCurrentRotation + " FR = " + FRCurrentRotation + " BL = " + BLCurrentRotation + " BR = " + BRCurrentRotation);
    	}   	
    }
    
    public void mecanumDrive(double driveX,double driveY,double rotation) {
    	double gyroAngle = 0; // Do not use a gyro to implement field-oriented driving
    	// Make sure we are in Mecanum mode:
   		this.setMecanumMode();
   		
    	// We want to call this function for autonomous, and want the input assumptions to be non-joystick.
    	// (ie. forward motion is +1 value).
    	// Because the library for Mecanum (only) assumes joystick, we invert here (And the library inverts it back)
		robotDrive41.mecanumDrive_Cartesian(driveX, -driveY, rotation, gyroAngle ); // WPILIB ASSUMES Joystick input (Y forward == -1)
    	printMXPInfo();

    	if (Robot.isDbgOn()) {
    		// Print the encoder values
    		// We use the "backdoor" method so that we don't force the controller to use the PID.
    		// The backdoor doesn't use the reverseSensor() function, so we have to do it manually here.
    		// While the "selected sensor" uses units of rotation, the backdoor uses only natie units of 4*CPR (counts per revolution).
    		double FLCurrentRotation = -RobotMap.driveTrainFLMotor.getEncPosition()/2048.0; // Returns 4*1024 = 4096 units per rev
    		double FRCurrentRotation = RobotMap.driveTrainFRMotor.getEncPosition()/2048.0;
    		double BLCurrentRotation = -RobotMap.driveTrainBLMotor.getEncPosition()/2048.0;
    		double BRCurrentRotation = RobotMap.driveTrainBRMotor.getEncPosition()/2048.0;
    		
    		System.out.println("FL = " + FLCurrentRotation + " FR = " + FRCurrentRotation + " BL = " + BLCurrentRotation + " BR = " + BRCurrentRotation);
    	}
}

	public void octaCanumDriveWithJoysticks(Joystick joyLeft, Joystick joyRight) {
		// Extract the joystick values
		double joyLeftX, joyLeftY, joyRightX, joyRightY;
		
		// If an Xbox controller, try using the two sticks on controller 1 (Right side) instead of using two joysticks
		if (joyRight.getIsXbox()) {
			joyLeftX = joyRight.getRawAxis(0);  // Left stick X
			joyLeftY = joyRight.getRawAxis(1);  // Left stick Y
			joyRightX = joyRight.getRawAxis(4); // Right stick X
			joyRightY = joyRight.getRawAxis(5); // Right stick Y
		}
		else {
			joyLeftX  = joyLeft.getX();
			joyLeftY  = joyLeft.getY();
			joyRightX = joyRight.getX();
			joyRightY = joyRight.getY();
		}

		// Print the raw joystick inputs
		//System.out.println("joyLeftY="+joyLeftY+" joyLeftX="+joyLeftX + " joyRightY="+joyRightY+" joyRightX="+joyRightX);

		// Apply the 'dead zone' guardband to the joystick inputs:
		// Centered joysticks may not actually read as zero due to spring variances.
		// Therfore, remove any small values as being "noise".
		double joystickDeadzone = SmartDashboard.getNumber("Joystick Deadzone", 0); // default if entry not found
		if (Math.abs(joyLeftX) < joystickDeadzone)
			joyLeftX = 0;
		if (Math.abs(joyLeftY) < joystickDeadzone)
			joyLeftY = 0;
		if (Math.abs(joyRightX) < joystickDeadzone)
			joyRightX = 0;
		if (Math.abs(joyRightY) < joystickDeadzone)
			joyRightY = 0;
		
		
		// Find out which operating mode is requested
		// Complicated state machine.  When using the left joystick to crab, treat it as a temporary excursion into mecanum.
		// Must return to traction when stick is released, but this requires knowing whether we were in mecanum or traction before the crab operation.
		// (Call this isCrabExcursion; if true then we are in this funny state.
		// This operation is basically a 5-input state machine.
		// We need the force_mecanum, toggle, crab, mecanum, and joyleft inputs
		// next-state indicators for the four possible transitions (not encoded)
		boolean normal    = false;
		boolean enterCrab = false;
		boolean exitCrab  = false;
		boolean stayCrab  = false;
		
		// simplified input variable
		boolean isCrabbing = (joyLeftX != 0);
		
		// Transition table (illegal states and don't-cares are not listed, of course)
		if      (!m_isCrabExcursion && !isInMecanumMode() && !isCrabbing) //000
			normal = true;
		else if (!m_isCrabExcursion && !isInMecanumMode() &&  isCrabbing) //001
			enterCrab = true;
		else if (!m_isCrabExcursion &&  isInMecanumMode()               ) //010 or 011
			normal = true;			
		else if ( m_isCrabExcursion &&  isInMecanumMode() && !isCrabbing) //110
			exitCrab = true;
		else if ( m_isCrabExcursion &&  isInMecanumMode() &&  isCrabbing) //111
			stayCrab = true;
		else //when all else fails, behave normally as if there is no crab excursion mode!
			normal = true;  
		
		// There are other factors too-- do these just to ensure a sane state
		// if we decide to toggle while in a crab excursion, clear the crab bit. (done in this.toggleDriveTrain())
		// if we decide to force mecanum while in a crab excursion, clear the crab bit. (Done in the ForceMecanum command)
		
		// Now that we know the next state, execute on it:
		if (normal) {
			if (!this.isInMecanumMode()) {
				// Here, we are in arcade mode, and are not using the crab joystick.   Use the traction wheels.
				this.arcadeDrive(-joyRightY, joyRightX); // fwd/rvs, rotation (CW/right is negative)
			}
			else {
				// Drive with the mecanum wheels...
				this.mecanumDrive(joyLeftX, -joyRightY, joyRightX); // X motion (crab), Y motion (fwd/rvs), rotation
			}
		}
		else if (enterCrab) {
			// set the crab bit, and call the mecanum drive
			m_isCrabExcursion = true;
			this.mecanumDrive(joyLeftX, -joyRightY, joyRightX); // X motion (crab), Y motion (fwd/rvs), rotation
		}
		else if (exitCrab) {
			// Clear the crab bit, and call the traction drive
			m_isCrabExcursion = false;
			this.arcadeDrive(-joyRightY, joyRightX); // fwd/rvs, rotation (CW/right is negative)				
		}
		else if (stayCrab) {
			// continue to drive in mecanum mode
			this.mecanumDrive(joyLeftX, -joyRightY, joyRightX); // X motion (crab), Y motion (fwd/rvs), rotation
		}
		SmartDashboard.putBoolean("CrabExcursion", m_isCrabExcursion);

	}
	
    // Function to STOP the drivetrain:
    public void stop() {
    	// Call the WPILIB code directly so we don't change modes when stopping...
    	robotDrive41.arcadeDrive(0, 0, false); //move, rotate, squaredInputs
    }
    
    public boolean isInMecanumMode() {
    	// Simply return the state of the global variable
    	return this.m_isInMecanumMode;
    }
    
    // All the things needed to enter mecanum mode
    public void setMecanumMode() {
    	// Set the global variable
    	this.m_isInMecanumMode = true;
    	
    	// Set the piston state
		this.solenoid.set(false); // release the pneumatics; gravity + "springs" will drop us back onto default mecanum wheels.
    	
    	// Set the correct motor inversion
		robotDrive41.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		robotDrive41.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    	
		// Set the dashboard indicator
		SmartDashboard.putString("Drivetrain Mode", this.m_isInMecanumMode?"MECANUM":"TRACTION");	
    }
    
    // All the things needed to enter mecanum mode
    public void setTractionMode() {
    	// Set the global variable
    	this.m_isInMecanumMode = false;
    	
    	// Set the piston state
		this.solenoid.set(true); // engage the pneumatics to force the traction wheels down
    	
    	// Set the correct motor inversion
		robotDrive41.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
		robotDrive41.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
    	
		// Set the dashboard indicator
		SmartDashboard.putString("Drivetrain Mode", this.m_isInMecanumMode?"MECANUM":"TRACTION");	
    }
    
    public void toggleDrivetrain() {
    	// Toggle the state of the driveline mode
		if (Robot.driveTrain.isInMecanumMode())
			Robot.driveTrain.setTractionMode();
		else // Must be in traction mode, so...
			Robot.driveTrain.setMecanumMode();
		// Clear any temporary crab excursion state
		clearCrabExcursion();
    }
    
    // For Gyro information shared by multiple subsystems
    // (Perhaps move to separate subsystem?)
    public void printMXPInfo() {
    	AHRS ahrs = Robot.ahrs; // this creates a local variable whose scope overrides the one in Robot; makes cut and paste from MXP examples easier
        /* These functions are compatible w/the WPI Gyro Class, providing a simple  */
        /* path for upgrading from the Kit-of-Parts gyro to the navx MXP            */
        
        SmartDashboard.putNumber(   "Raw Angle",         ahrs.getAngle()); //cumulative over time
        SmartDashboard.putNumber(   "Rotation Rate",       ahrs.getRate());
 
        /* Display 9-axis Heading (requires magnetometer calibration to be useful)  */
        //SmartDashboard.putNumber(   "IMU_FusedHeading",     ahrs.getFusedHeading());

        /* Display estimates of velocity/displacement.  Note that these values are  */
        /* not expected to be accurate enough for estimating robot position on a    */
        /* FIRST FRC Robotics Field, due to accelerometer noise and the compounding */
        /* of these errors due to single (velocity) integration and especially      */
        /* double (displacement) integration.                                       */
        
        //SmartDashboard.putNumber(   "Velocity_X",           ahrs.getVelocityX());
        //SmartDashboard.putNumber(   "Velocity_Y",           ahrs.getVelocityY());
        //SmartDashboard.putNumber(   "Displacement_X",       ahrs.getDisplacementX());
        //SmartDashboard.putNumber(   "Displacement_Y",       ahrs.getDisplacementY());

    }
    
    // Used by external classes to cancel temporary excursions into crab mode (eg. forceMecanum and Toggle)
    public void clearCrabExcursion() {
    	m_isCrabExcursion = false;
    }
        
    @Override
    /* This function is invoked periodically by the PID Controller, */
    /* based upon navX MXP yaw angle input and PID Coefficients.    */
    public void pidWrite(double output) {
        m_rotateToAngleRate = output;
    }

    // Function to expose the PID-written output for command consumption
    public double getPIDRotationRate() {
    	return m_rotateToAngleRate;
    }

    // Member Variables
    boolean m_isInMecanumMode; // True = mecanum; false = traction
    boolean m_isCrabExcursion; // Keep track of whether we are temporarily crabbing from traction mode
    
    //--------------------------------
    // HW PID for GearVision mode
    //--------------------------------
    
    // Routine to switch hardware into a hardware PID mode for gear delivery
     public void setGearMode() {
    	// Grab current operation mode and save it off for later if necessary
    	m_savedTalonMode = fLMotor.getControlMode();
    	
    	// We already set up the sensors in drivetrainInit()
    	
    	// No configEncoderCodesPerRev is needed for CTRE Mag Encoder.
    	fLMotor.configNominalOutputVoltage(+0f,  -0f);
    	fLMotor.configPeakOutputVoltage(+2.4f,  -2.4f);  // corresponds to a -1:1 scale of 0.2 for max output.
    	
    	// @FIXME:  We might need to add a voltage ramp rate if we accellerate too quickly
    	//fLMotor.setVoltageRampRate(something)
    	
    	/* set the allowable closed-loop error,
         * Closed-Loop output will be neutral within this range.
         * See Table in Section 17.2.1 for native units per rotation. 
         */
    	// Assume 0.5" error.  and 4" diameter wheel.
    	// one rotation is pi*d = 12.5664"; assume 512 CPR (docs say 1024, but that appears to be 2x off?)
    	fLMotor.setAllowableClosedLoopErr((int) Math.round(m_gearErrVal*2048));// wants units of 4*CPR.
    	/* set closed loop gains in slot0 qnd slot1*/
    	// Note that these can be set via roborio web interface for dynamic tuning...
        fLMotor.setProfile(1); // Short range
        fLMotor.setF(0);
        fLMotor.setP(10.0);
        fLMotor.setI(0.0); 
        fLMotor.setD(0.0);

        fLMotor.setProfile(0); // Medium range
        fLMotor.setF(0);
        fLMotor.setP(4.0);
        fLMotor.setI(0.0); 
        fLMotor.setD(0.0);
        
        
        // Set FL as the master, and the rest as followers of its magnitude
        fLMotor.changeControlMode(TalonControlMode.Position);
        fRMotor.changeControlMode(TalonControlMode.Follower);
        bLMotor.changeControlMode(TalonControlMode.Follower);
        bRMotor.changeControlMode(TalonControlMode.Follower);
        
        // Get the follower output inversions correct.
        // To crab left:
        // FL = Reverse, FR = Forward.
        // BL = Forward, BR = Reverse.
        // 
        // Therefore, if FL is the master, FR and BL must be output inverted for crabbing
        fRMotor.reverseOutput(true);
        bLMotor.reverseOutput(true);

        // When entering this mode, zero out the "position" of the sensor
        fLMotor.setPosition(0);
        
        // Initial setpoint of zero (meaning no motion)
        fLMotor.set(0);
        
        // Followers do whatever master does:
        fRMotor.set(fLMotor.getDeviceID());
        bLMotor.set(fLMotor.getDeviceID());
        bRMotor.set(fLMotor.getDeviceID());
   	
    }

     public void setDriveMode() {
    	 // Return PID mode to the normal drive-by-joystick behavior:
         fLMotor.changeControlMode(m_savedTalonMode);
         fRMotor.changeControlMode(m_savedTalonMode);
         bLMotor.changeControlMode(m_savedTalonMode);
         bRMotor.changeControlMode(m_savedTalonMode);

         this.stop();
     }
     
     public boolean gearOnTarget() {
    	 return (fLMotor.getClosedLoopError() <= m_gearErrVal);
     }
     
     // Just set position PID to whatever the gear system requests...
     // This is the number of rotations from the zeroed position.
     public void setGearSetpoint(double target) {
    	 fLMotor.set(target);
     }
    
    //--------------------------------
    // SW PID subsystem variables
    //--------------------------------
    public PIDController drivelineController; // Use this PID controller to accomplish turns and track straight when driving forward.
    double m_rotateToAngleRate; // PID output tells us how much to rotate to reach the desired setpoint target.
    
    /* The following Software PID Controller coefficients will need to be tuned */
    /* to match the dynamics of your drive system.  Note that the      */
    /* SmartDashboard in Test mode has support for helping you tune    */
    /* controllers by displaying a form where you can enter new P, I,  */
    /* and D constants and test the mechanism.                         */
    
    static final double kP = 0.05;
    static final double kI = 0.00;
    static final double kD = 0.00;
    static final double kF = 0.00;
    static final double kToleranceDegrees = 0.5f; // Stop if we are within this many degrees of the setpoint.
    
    // Hardware PID related variables
    TalonControlMode m_savedTalonMode;
	static final double m_gearErrVal = (0.5)/(3.1415927*4);
    
    // wheel distance per revolution should be circumference, but should empirically verified.
    // We are SWAGging that crabbing will be sqrt2/2 factor off this.
    public static final double m_inchesPerRevolution = 3.1415927*4; // for starters, use circumference of 4" wheel

}

