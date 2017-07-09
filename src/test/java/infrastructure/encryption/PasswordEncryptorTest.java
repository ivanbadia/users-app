package infrastructure.encryption;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class PasswordEncryptorTest {


    @Test
    public void null_text_should_not_be_allowed(){
        //Given
        String text = null;

        //Then
        assertThatThrownBy(() -> PasswordEncryptor.encrypt(text))
                                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_generate_an_encoded_sha256(){
        //Given
        String text = "Message Digest";

        //When
        String encryptedText = PasswordEncryptor.encrypt(text);

        //Then
        assertThat(encryptedText)
                .isEqualTo("GMlByArwtBK/bmaKCaY+FAqSMSdOySeFHlapmPoh50c=");
    }

}