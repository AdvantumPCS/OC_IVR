/*
 * Created on Jan 27, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pcs.recruit.access;

/**
 * @author Orville
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface MessageId
{

    public static final int WORK = 10;
    public static final String WAV_WORK_MISSING="voices/workMissing.wav";
    public static final String TEXT_WORK_MISSING="You have been assigned work, however the details are not available. " +
										"Please contact the recruiting center for information.";
    public static final String SMS_WORK_MISSING="Work is assigned, details not available. Contact recruiting center.";
    
    public static final String WAV_WORK_DOUBLE="voices/workDouble.wav";
    public static final String TEXT_WORK_DOUBLE="You have indicated that you are available for doubling";
    public static final String WAV_WORK_NODOUBLE="voices/workNoDouble.wav";
    public static final String TEXT_WORK_NODOUBLE="You have indicated that you are not available for doubling";
    
    
    public static final int DAY_OFF_THISWEEK = 7;
    public static final int DAY_OFF_NEXTWEEK = 8;
    
    public static final int LOGIN_INCORRECT =1;
    public static final String WAV_INCORRECT="voices/incorrect.wav";
    public static final String TEXT_INCORRECT="The Man or Pin number you entered is incorrect." +
										"Try again.";
    public static final String SMS_INCORRECT="Man or PIN number is incorrect. Correct and try later.";
    

    public static final int NO_WORK = 11;
    public static final String WAV_NOWORK="voices/nowork.wav";
    public static final String TEXT_NOWORK="No work has been assigned to you." +
										"Please try again later.";
    public static final String SMS_NOWORK="No work assigned. Try again later.";
    
    

    public static final int NO_MSG_SPECEFIED = -1;
    public static final String WAV_NOMSG="voices/nomsg.wav";
    public static final String TEXT_NOMSG="The details of this message are not available on the system." +
										"Please contact the recruiting center for details.";
    public static final String SMS_NOMSG="This message details is not available. Contact the recruiting center for details.";
    
    
    
    public static final int OUTSIDE_TIME= 6;
    public static final String WAV_OUTSIDE="voices/outsideTime.wav";
    public static final String TEXT_OUTSIDE="You have called in outside of the recruiting period." +
										"For Registered Workers, recruiting is from 10 a m to 12 p m and from 2 p m to 4 p m. " +
										"For Casual Workers, recruiting is from 11 a m to 1 p m and from 3 30 p m to 4 30 p m. " +
										"Please try again during the next recruiting period.";
    public static final String SMS_OUTSIDE="Your call is outside of the recruting time. Recruting is from 10 am to noon and 2 pm to 4 pm. " +
    										"Call during next period.";

    
    public static final int LAST_ATTEMPT =2;
    public static final String WAV_LASTATTEMPT="voices/lastAttempt.wav";
    public static final String TEXT_LASTATTEMPT="The Man or Pin number you entered is incorrect." +
										"This is your last attempt.";
    public static final String SMS_LASTATTEMPT="Man or Pin number is incorrect. Correct and Try again later.";
    
    public static final int LAST_GOODBYE =3;
    public static final String WAV_LASTBYE="voices/lastbye.wav";
    public static final String TEXT_LASTBYE="You have exceeded the allowable attempts." +
										"Please contact the recruiting center for assistance. Good-bye.";
    public static final String SMS_LASTBYE="You exceeded the maximum attempts. Contact the recruiting center for help";
    
    
    public static final int BARRED = 4;
    public static final String WAV_BARRED="voices/barred.wav";
    public static final String TEXT_BARRED="You have been barred from using the recruiting I V R system." +
										"Please contact the recruiting center for assistance.";
    public static final String SMS_BARRED="You are barred from using the IVR. Contact recuiting center for details.";

    
    
    public static final int SYS_ERROR= 800;
    public static final String WAV_SYSERROR="voices/sysError.wav";
    public static final String TEXT_SYSERROR="There is a technical problem with the recruiting system. " +
										"Please contact the recruiting center for further instructions.";
    public static final String SMS_SYSERROR="There are tecnical problems. Contact recruting center for instructions.";
    
    public static final int LIMIT_EXCEEDED=801;
    public static final String WAV_EXCEEDED="voices/limitExceeded.wav";
    public static final String TEXT_EXCEEDED="There are too many messages to be played. " +
										"Please contact the recruiting center for your messages.";
    public static final String SMS_EXCEEDED="Too many messages. Contact recruiting center for messages.";
    
    public static final int UNAVAILABLE=802;
    public static final String WAV_UNAVAILABLE="voices/sysUnavailable.wav";
    public static final String TEXT_UNAVAILABLE="The recruiting system is currently unavailable. " +
										"Please try again later or contact the recruiting center for further instructions."; 
    public static final String SMS_UNAVAILABLE="Recruiting system un-available. Try again later or contact recruiting center.";

    
        public abstract boolean isValidMessage();
    

}
