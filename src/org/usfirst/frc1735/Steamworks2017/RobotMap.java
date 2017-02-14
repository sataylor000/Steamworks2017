// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc1735.Steamworks2017;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static CANTalon driveTrainFLMotor;
    public static CANTalon driveTrainFRMotor;
    public static CANTalon driveTrainBLMotor;
    public static CANTalon driveTrainBRMotor;
    public static RobotDrive driveTrainRobotDrive41;
    public static Compressor driveTrainCompressor;
    public static Solenoid driveTrainSolenoid;
    public static PowerDistributionPanel pDPPowerDistributionPanel;
    public static CANTalon turretTurretTurner;
    public static CANTalon shooterShootMaster;
    public static CANTalon shooterShootFollower;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public static void init() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        driveTrainFLMotor = new CANTalon(1);
        LiveWindow.addActuator("Drive Train", "FLMotor", driveTrainFLMotor);
        
        driveTrainFRMotor = new CANTalon(2);
        LiveWindow.addActuator("Drive Train", "FRMotor", driveTrainFRMotor);
        
        driveTrainBLMotor = new CANTalon(3);
        LiveWindow.addActuator("Drive Train", "BLMotor", driveTrainBLMotor);
        
        driveTrainBRMotor = new CANTalon(4);
        LiveWindow.addActuator("Drive Train", "BRMotor", driveTrainBRMotor);
        
        driveTrainRobotDrive41 = new RobotDrive(driveTrainFLMotor, driveTrainBLMotor,
              driveTrainFRMotor, driveTrainBRMotor);
        
        driveTrainRobotDrive41.setSafetyEnabled(true);
        driveTrainRobotDrive41.setExpiration(0.1);
        driveTrainRobotDrive41.setSensitivity(0.5);
        driveTrainRobotDrive41.setMaxOutput(1.0);
        driveTrainRobotDrive41.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        driveTrainRobotDrive41.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        driveTrainCompressor = new Compressor(0);
        
        
        driveTrainSolenoid = new Solenoid(0, 0);
        LiveWindow.addActuator("Drive Train", "Solenoid", driveTrainSolenoid);
        
        pDPPowerDistributionPanel = new PowerDistributionPanel(0);
        LiveWindow.addSensor("PDP", "PowerDistributionPanel", pDPPowerDistributionPanel);
        
        turretTurretTurner = new CANTalon(5);
        LiveWindow.addActuator("Turret", "TurretTurner", turretTurretTurner);
        
        shooterShootMaster = new CANTalon(6);
        LiveWindow.addActuator("Shooter", "ShootMaster", shooterShootMaster);
        
        shooterShootFollower = new CANTalon(7);
        LiveWindow.addActuator("Shooter", "ShootFollower", shooterShootFollower);
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }
}
