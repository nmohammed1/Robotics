package team1649;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends SampleRobot
{
	//Declare all global variables
    RobotDrive robotDrive;
    Joystick stick; //Controller
    boolean isMecanum = true;
    VictorSP leftMotor;
    VictorSP rightMotor;
    VictorSP arm;
    Compressor pneumatics; 
    boolean allowMecanum = false; //Change this to allow mecanum again
    Solenoid solenoid;


    // Channels for the wheels
    final int frontLeftChannel	= 0; // 0
    final int rearLeftChannel	= 1; // 5
    final int frontRightChannel	= 2; // 1
    final int rearRightChannel	= 3; // 3
    final int leftMotorChannel = 4;  // 4
    final int rightMotorChannel = 5; // 2
    final int victorArmChannel = 6;  // 6
    final int pwmRight = 1;
    final int pwmLeft = 0;

    /**
     * From left to right
     * PWM 0-  
     * PWM 1-
     * PWM 2-
     * PWM 3- 
     * PWM 4- 4
     * PWM 5- 5
     * PWM 6- 6
     */
    
    // The channel on the driver station that the joystick is connected to
    final int joystickChannel	= 0;
    
    public Robot() 
    {
        //robotDrive = new RobotDrive(frontLeftChannel, rearLeftChannel, frontRightChannel, rearRightChannel);
        robotDrive = new RobotDrive(pwmLeft ,pwmRight);
    	//robotDrive.setInvertedMotor(MotorType.kFrontLeft, false);	// invert the left side motors
    	//robotDrive.setInvertedMotor(MotorType.kRearLeft, false);		// you may need to change or remove this to match your robot
        robotDrive.setExpiration(0.1);
        
        leftMotor = new VictorSP(leftMotorChannel);
        rightMotor = new VictorSP(rightMotorChannel);
        
        pneumatics = new Compressor(); // Default is 0.
        solenoid = new Solenoid(1);
        arm = null;
    
        stick = new Joystick(joystickChannel);        
    }
    
    
    private void startPneumatics()
    {
    	pneumatics.start();
    	pneumatics.setClosedLoopControl(true);
    	
	    SmartDashboard.putBoolean("Pneumatics Enabled", pneumatics.enabled());	
    }
    
    public void test()
    {
    	
    }
    
    public void a_turnLeft(int times)
    {
    	for (int i = 0; i < times; i++)
    	{
        	robotDrive.drive(1.0, 0.5);
            Timer.delay(0.1);
    	}
    }
    
    public void autonomous()
    {
    	a_turnLeft(10);
        
        
        robotDrive.drive(0.0, 0.0);	// stop robot
    }	
        
    public void operatorControl() 
    {
        robotDrive.setSafetyEnabled(true);
        
        //startPneumatics();
  
        
        while (isOperatorControl() && isEnabled())
        {
        	
        	if (stick.getRawButton(2))
    		{
    			isMecanum = false;
    		}
            
        	if ((stick.getRawButton(1) || isMecanum) && allowMecanum) // Not Used
        	{
        		isMecanum = true;
        		robotDrive.mecanumDrive_Cartesian(stick.getX(), stick.getY(), getZ(), 0);
        	}
        	else
        	{
    	       	robotDrive.tankDrive(leftSpeed(), rightSpeed());
    	     	leftMotor.set(leftSpeed());
    	      	rightMotor.set(rightSpeed());
        	}
        	if (stick.getRawButton(3))
        	{ 	
        		solenoid.set(true);
        		//pneumatics.setClosedLoopControl(true);
        	}
        	
        	if (stick.getRawButton(4))
        	{
        		solenoid.set(false);
        		//pneumatics.setClosedLoopControl(false);
        	}
       
	        
	        SmartDashboard.putNumber("leftSpeed(): ", leftSpeed());
	        SmartDashboard.putNumber("rightSpeed(): ", rightSpeed());
	        SmartDashboard.putBoolean("rightMotor isAlive", rightMotor.isAlive());
	        SmartDashboard.putBoolean("leftMotor isAlive", leftMotor.isAlive());
	        SmartDashboard.putBoolean("Solenoid", solenoid.get());
	        //SmartDashboard.putBoolean("Solenoid", ! pneumatics.getClosedLoopControl());

	        
	        //SmartDashboard.putBoolean("isMecanum: ", isMecanum);
	       	
	        Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles
          
        }    
    }
    
    private double leftSpeed()
    {	
    	double speed = stick.getRawAxis(1) * - 1;
    	
    	if ((speed <= 0.25) & (speed >=- 0.25))
    	{
    		return 0.0;
    	}
    	else
    	{
        	return speed;
    	}
    }
    
    private double leftSpeed(int axis)
    {	
    	double speed = stick.getRawAxis(axis) * - 1;
    	
    	if ((speed <= 0.25) & (speed >=- 0.25))
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
    	double speed = stick.getRawAxis(5) * - 1;
    	
    	if ((speed <= 0.25) & (speed >=- 0.25))
    	{
    		return 0.0;
    	}
    	else
    	{
        	return speed;
    	}
    }

    private double rightSpeed(int axis)
    {
    	double speed = stick.getRawAxis(axis) * - 1;
    	
    	if ((speed <= 0.25) & (speed >=- 0.25))
    	{
    		return 0.0;
    	}
    	else
    	{
        	return speed;
    	}
    }
    /**
     * Only used in Mecanum Drive which is currently disabled
     */
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

