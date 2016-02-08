// Pentcho Tchomakov - 260632861

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// We don't need methods for this part of the assignment. It was possible to add them, but it added complexity,
// Especially when C is very stuck-up with passing some types of data
int main(int argc, char *argv[]){
    
    FILE *incoming;
    FILE *outcoming;
    int c;
    int d;
    int countsize = 0;
    int sidetrack[1000];
    
    // We first check if we have 4 arguments
    if(argc != 4) {
        fprintf(stderr, "usage: %s <incoming_file> <outgoing_file> <chat_handle> \n", argv[0]);
        return EXIT_FAILURE;
    }
    // And if the incoming file is equal to the outgoing file.
    else if(strcmp(argv[1], argv[2]) == 0) {
        return EXIT_FAILURE;
    }
    
    // This is where the chat starts
    // We put the logic in a "while loop" because we want to keep the programming going until
    // Someone dose ctrl + D or closes the terminal.
    while(1) {
        // First thing we check is if the files exist. If reading the file == NULL, then the file doesn't exist.
        if((incoming = fopen(argv[1], "r")) == NULL){
            printf("ERROR: The incoming file cannot be opened!");
            return EXIT_FAILURE;
        }
        
        // We create a string/char of lenght 1000; enough for our chat purposes
        // We then add a pointer to the that array of chars. It's easier to handle the char arrays this ways
        char inMessage[1000];
        char *inPointer = inMessage;
        
        while((c = fgetc(incoming)) != EOF)
        {
            *inPointer = c;
            inPointer++;
        }
        
        // Reset the pointer as null or nextLine = null;
        *inPointer = '\0';
        
        // We get the size of the string of the text file
        // Also, we create a pointer to go through the text file line by line.
        size_t length = strlen(inMessage);
        sidetrack[countsize] = length;
        
        // If the length of the string in the file is 0, then nothing has been sent from the other terminal
        if(length == 0) {
            printf("Nothing received yet.\n");
        }
        // If the lenght of the string in the txt file is greater than 0, that means something has been written;
        // We print the message. Although, we check if our pointer for the lines already printed is 0. If it's 0, then we haven't received
        // Anything yet. We print everything
        if(length > 0 && countsize == 0) {
            printf("Received: %s \n", inMessage);
        }
        // If the length is not 0 and the pointer for the lines is not 0, we go find the lines which we need to print, without printing
        // What's already on the terminal
        if(length > 0 && sidetrack[countsize] == sidetrack[countsize - 1]);
        if(countsize != 0 && sidetrack[countsize] != sidetrack[countsize - 1])
            printf("Received:  %s\n", inMessage);
        // We update our pointer afterwards
        countsize ++;
        
        // This part is when we want to send a message
        printf("Send:   ");
        // Create a string-char array to store the message we want to send and a corresponding pointer to ease its usage
        char outMessage[1000];
        char *outPointer = outMessage;
        
        while((d = getchar()) != '\n' && d != EOF ){
            
            *outPointer = d;
            outPointer++;
        }
        
        // The pointer resets to NULL value
        *outPointer = '\0';
        
        // We test if the file exists first. If it doesn't, we exist.
        if((outcoming = fopen(argv[2], "w")) == NULL)
        {
            fprintf(stderr, "The file %s cannot be opened. \n", argv[2]);
            fclose(incoming);
            return EXIT_FAILURE;
        }
        
        // If everything goes well, we print the message we just wrote in the outgoing text file for the other terminal to read it.
        fprintf(outcoming, "[%s]:  %s", argv[3], outMessage);
    }
    return 0;
}
