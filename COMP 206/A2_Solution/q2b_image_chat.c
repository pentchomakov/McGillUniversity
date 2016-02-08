// Pentcho Tchomakov - 260632861

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

// It's easier to do all of this assignment in the main with no methods
int main(int argc, char* argv[])
{
    // File pointers for our files
    FILE *imgIncoming = NULL;
    FILE *imgOutgoing = NULL;
    // Some Variables
    int write;
    int sizetrack[1000];
    int counter = 0;
    
    // Decoder variables
    const int bmp_header_size = 54;
    const int max_msg_size = 1000;
    int i;
    int c;
    int imageIdx = 0;
    int messageIdx = 0;
    int bitIdx = 0;
    char msg[max_msg_size];
    
    // Encoder variables
    int messageId2 = 0;
    int imageId2 = 0;
    int bitId2 = 0;
    char center2;
    
    int reading;
    int messageId = 0;
    int imageId = 0;
    int bitId = 0;
    char center;
    
    // If we do not have 4 arguments, then the user didn't input enough arguments in the command line.
    // Also, the 2nd and the 3rd argument have to be different; the incoming file cannot be the same as the
    // outgoing file
    // Program exists if any of those conditions fail
    if(argc != 4 || strcmp(argv[1],argv[2]) == 0) {
        fprintf(stderr, "Usage: %s <incoming_file> <outgoing_file> <chat_handle>\n", argv[0]);
        return EXIT_FAILURE;
    }
    
    // Infinite loop to start the conversation between both terminals
    while(1) {
        // We open the incoming file from the arguments passed [1]
        imgIncoming = fopen(argv[1], "rb");
        
        // If we can't open the incoming file, then it's time to send
        while(imgIncoming == NULL) {
            printf("Nothing received yet.\n");
            printf("Send: ");
        
            // char for the received message + its pointer (q2a)
            char output1[1000];
            char *point1 = output1;
        
            // We get all the chars until there is a next line
            while((write = getchar()) != '\n'){
                *point1 = write;
                point1++;
            }
            *point1 = '\0';
        
            // If we can't open the file, we get a NULL, and we must exit
            imgIncoming = fopen(argv[1], "rb" );
            if(imgIncoming == NULL) {
                printf("The input image file cannot be opened. \n" );
                return EXIT_FAILURE;
            }
        
            // If we can't open the file, we get a NULL, and we must exit
            imgOutgoing = fopen(argv[2], "wb");
            if(imgOutgoing == NULL) {
                printf("The output file cannot be opened. \n");
                return EXIT_FAILURE;
            }
        
            // Encoding the image
            while((reading = fgetc(imgIncoming)) != EOF) {
                center2 = (char)reading;
            
                if(imageId2 >= bmp_header_size && messageId2 <= strlen(output1)) {
                    char bit_mask_en1 = 1 << bitId2;
                
                    if((output1[messageId2] & bit_mask_en1) > 0) {
                        center2 |= 1;
                    }
                    else {
                        center2 &= 254;
                    }
                    bitId2++;
                
                    if(bitId2 >= 8) {
                        bitId2 = 0;
                        messageId2++;
                    }
                }
            
                fputc(center2, imgOutgoing);
                imageId2++;
            }
            
            // We close the files safely
            fclose(imgOutgoing);
            fclose(imgIncoming);
    }
    
        // We reading the received image
        for(i = 0; i < max_msg_size; i++) {
            msg[i] = 0;
        }
        
        // We decode the image
        while((c = fgetc(imgIncoming)) != EOF){
        
            if(imageIdx >= bmp_header_size){
                char bit_mask = 0;
                if((c & 1)  > 0 )
                    bit_mask |= 1 << bitIdx;
                msg[messageIdx] |= bit_mask;
                bitIdx++;
                if(bitIdx >= 8){
                    if( msg[messageIdx] == '\0' )
                        break;
                    bitIdx = 0;
                    messageIdx++;
                }
            }
        
            imageIdx++;
        }
    
        // If nothing is received we proceed here.
        size_t length = strlen(msg);
        sizetrack[counter] = length;
    
        if(length == 0)
            printf("Nothing received yet.\n");
    
        // Print message (from q2a)
        if(length > 0 && counter == 0) {
            printf("Received: %s\n", msg);
        }
        
        // We
        if(sizetrack[counter] == sizetrack[counter - 1]);
        if(counter != 0 && sizetrack[counter] != sizetrack[counter-1]) {
            printf("Received: %s\n", msg);
        }
        counter ++;
    
        // Writing to the output file here
        printf("Send: ");
        
        // Char for output message + pointer
        char output[1000];
        char *point = output;
        
        // Gather the chars for a string to write. Until there is nothing
        while((write = getchar()) != '\n'){
            *point = write;
            point++;
        }
        *point = '\0';
        
        // If the files do not exist, we exist
        imgIncoming = fopen(argv[1], "rb");
        if(imgIncoming == NULL){
            printf( "Cannot open input file. \n" );
            return EXIT_FAILURE;
        }
        
        // Same here
        imgOutgoing = fopen(argv[2], "wb");
        if(imgOutgoing == NULL){
            printf("Cannot open output file . \n");
            return EXIT_FAILURE;
        }
    
        // Encoding image
        while((reading = fgetc(imgIncoming)) != EOF){
            center = (char)reading;
        
            if(imageId >= bmp_header_size && messageId <= strlen(output)) {
                char bit_mask_en = 1 << bitId;
            
                if((output[messageId] & bit_mask_en) > 0) {
                    center |= 1;
                }
                else {
                    center &= 254;
                }
                bitId++;
            
                if(bitId >= 8) {
                    bitId = 0;
                    messageId++;
                }
            }
        
            fputc(center, imgOutgoing);
            imageId++;
        }
    
        // Safely close the files
        fclose(imgOutgoing);
        fclose(imgIncoming);
    }
}
