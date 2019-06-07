
package cityproject;

import com.darkprograms.speech.synthesiser.SynthesiserV2;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import static java.awt.SystemColor.text;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;


public class VoiceAssistant {
    
    private Voice voice;
    private final String VOICENAME = "kevin16";
    
    public VoiceAssistant()
    {
        
    }    
    
    public void speakUp(String text) throws IOException
    {
        VoiceManager vm = VoiceManager.getInstance();
        
        voice = vm.getVoice(VOICENAME);
        
        voice.allocate();

        voice.speak(text);    
    } 
    
}
