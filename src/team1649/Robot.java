package team1649;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends SampleRobot
{
    RobotDrive robotDrive;
    Joystick stick; //Controller
    boolean isMecanum = true;
    //VictorSP motorTest;
    //Compressor numatics;

    // Channels for the wheels
    final int frontLeftChannel	= 0;
    final int rearLeftChannel	= 1;
    final int frontRightChannel	= 2;
    final int rearRightChannel	= 3;
    
    // The channel on the driver station that the joystick is connected to
    final int joystickChannel	= 0;
  
    
    public Robot() 
    {
        robotDrive = new RobotDrive(frontLeftChannel, rearLeftChannel, frontRightChannel, rearRightChannel);
    	robotDrive.setInvertedMotor(MotorType.kFrontLeft, true);	// invert the left side motors
    	robotDrive.setInvertedMotor(MotorType.kRearLeft, true);		// you may need to change or remove this to match your robot
        robotDrive.setExpiration(0.1);
        //motorTest = new VictorSP(5);
        //numatics = new Compressor(); // Default is 0.
    
        stick = new Joystick(joystickChannel);
    }
    
    private void startPneumatics()
    {
    	//numatics.start();
    	//numatics.setClosedLoopControl(true);
    }
        
    //We can now choose between tank drive and mecanum in a click.
    public void operatorControl() 
    {
        robotDrive.setSafetyEnabled(true);
        
        while (isOperatorControl() && isEnabled())
        {
        	
        	if (stick.getRawButton(2))
    		{
    			isMecanum = false;
    		}
            
        	if (stick.getRawButton(1) || isMecanum)
        	{
        		isMecanum = true;
        		robotDrive.mecanumDrive_Cartesian(stick.getX(), stick.getY(), getZ(), 0);
        	}
        	else
        	{
    	       	robotDrive.tankDrive(leftSpeed(), rightSpeed());
        	}
        	
	           
        	//motorTest.set(leftSpeed());
	        
	        SmartDashboard.putNumber("leftSpeed(): ", leftSpeed());
	        SmartDashboard.putNumber("rightSpeed(): ", rightSpeed());

	        SmartDashboard.putBoolean("isMecanum: ", isMecanum);
	       	
	        Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles
          
        }    
    }
    
    private double leftSpeed()
    {	
    	double speed = stick.getRawAxis(1) * -1;
    	
    	if ((speed <= 0.25) & speed >=- 0.25)
    	{
    		return 0.0;
    	}
    	else
    	{
        	return speed;
    	}
    }
    
    private double rightSpeed()
    {
    	double speed = stick.getRawAxis(5);
    	
    	if ((speed <= 0.25) & speed >=- 0.25)
    	{
    		return 0.0;
    	}
    	else
    	{
        	return speed;
    	}
    }

    private double getZ()
    {
    	if(stick.getRawAxis(2) == 0)
    	{
    		return stick.getRawAxis(3) * - 1; // to make negative
    	}
    	if(stick.getRawAxis(3) == 0)
    	{
        	return stick.getRawAxis(2);	
    	}
    	return 0.0;
    }
    
}

