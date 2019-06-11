
package cityproject;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;





public class VoiceAssistant {
    
    private Voice voice;
    private final String VOICENAME = "kevin16";
    
    public VoiceAssistant()
    {
        
    }    
    
    public void speakUp(String text) 
    {
        VoiceManager vm = VoiceManager.getInstance();
        
        voice = vm.getVoice(VOICENAME);
        
        voice.allocate();

        voice.speak(text);    
    } 
    
    
// using maryTTS library   
//    private MaryInterface marytts;
//    private AudioPlayer ap;
//    private final String voiceName = "cmu-slt-hsmm";
//    
//    public VoiceAssistant()
//    {
//        try
//        {
//            marytts = new LocalMaryInterface();
//            marytts.setVoice(voiceName);
//            ap = new AudioPlayer();
//        }
//        catch (MaryConfigurationException ex)
//        {
//            ex.printStackTrace();
//        }
//    }
//
//    public void speakUp(String input) throws SynthesisException
//    {
//        AudioInputStream audio = marytts.generateAudio(input);
//        ap.setAudio(audio);
//        ap.start();
//    }       
    
}
