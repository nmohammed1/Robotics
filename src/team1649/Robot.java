package team1649;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends SampleRobot
{
    RobotDrive robotDrive;
    Joystick stick; //Controller

    // Channels for the wheels
    final int frontLeftChannel	= 0;
    final int rearLeftChannel	= 1;
    final int frontRightChannel	= 2;
    final int rearRightChannel	= 3;
    
    // The channel on the driver station that the joystick is connected to
    final int joystickChannel	= 0;
    private boolean[] deadZone = {false, false}; 
    boolean whileDrive = true;
  
    
    public Robot() 
    {
        robotDrive = new RobotDrive(frontLeftChannel, rearLeftChannel, frontRightChannel, rearRightChannel);
    	robotDrive.setInvertedMotor(MotorType.kFrontLeft, true);	// invert the left side motors
    	robotDrive.setInvertedMotor(MotorType.kRearLeft, true);		// you may need to change or remove this to match your robot
        robotDrive.setExpiration(0.1);
    
        stick = new Joystick(joystickChannel);
    }
        

    /**
     * Runs the motors with Mecanum drive.
     */
    public void operatorControl() 
    {
        robotDrive.setSafetyEnabled(true);
        
        while (isOperatorControl() && isEnabled())
        {
        	deadZone();
        	
            /*
        	for(int i = 0; i < deadZone.length; i++){
        		if(deadZone[i])
        		{
        			whileDrive = false;
        		}
        	}*/
            
	       	if(whileDrive)
	       	{
	       		robotDrive.mecanumDrive_Cartesian(stick.getX(), stick.getY(), getZ(), 0);
	        }
	               
	       	resetDeadzones();
	        whileDrive = true;
	        
	        SmartDashboard.putNumber("Joystick x", stick.getX());
	        SmartDashboard.putNumber("Joystick y", stick.getY());
	        SmartDashboard.putNumber("Joystick z", getZ());
	        
	        SmartDashboard.putBoolean("DeadZone 0", deadZone[0]);
	        SmartDashboard.putBoolean("DeadZone 1", deadZone[1]);
	       	
	        Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles
          
        }    
    }
    
    private void deadZone()
    {
    	if((stick.getX()<=0.25) && (stick.getX()>=-0.25))
    	{
    		deadZone[0] = true;
    	}
    	if((stick.getY()<=0.25) && (stick.getY()>=-0.25))
    	{
    		deadZone[1]= true;
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
    
    private void resetDeadzones()
    {
    	for(boolean dead : deadZone)
    	{
    		if(dead)
    		{
    			dead = false;
    		}
    	}
    }
}

