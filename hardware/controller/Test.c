#include <avr/io.h>
#include <avr/delay.h>



void main() {
    DDRB = 0b10001111;
    DDRC = 0b10111111;
    DDRD = 0b01101111;
    DDRE = 0b10111111;
    
    DDRF = 0b10000000;
    PORTF = 0x00;
    while(1){
        // button 1
        if((PIND & (1<<PIND4))){
            PORTF |= (1<<PORTF7);
        }
        // button 2
        else if (PINC & (1<<PINC6)){
            PORTF &= ~(1<<PORTF7);
        }
        // button 3
        else if (PIND & (1<<PIND7)){
            PORTF &= ~(1<<PORTF7);
        }
        // button 4
        else if (PINE & (1<<PINE6)){
            PORTF &= ~(1<<PORTF7);
        }
        // button 5
        else if (PINB & (1<<PINB4)){
            PORTF &= ~(1<<PORTF7);
        }
        // button 5
        else if (PINB & (1<<PINB5)){
            PORTF &= ~(1<<PORTF7);
        }
        // button 5
        else if (PINB & (1<<PINB6)){
            PORTF &= ~(1<<PORTF7);
        }
        _delay_ms(1);
	}
}
