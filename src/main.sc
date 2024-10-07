require: ./patterns.sc

theme: /
    
    state: Start
        q!: *start
        q!: $hello
        random:
            a: Hello, world!
            a: Greetings!
            a: Who's there?!
        go!: /BookAWorkplace
        
    state: CatchAll || noContext = true
        event!: noMatch
        random:
            a: Forstor ikke, men veldig interessant!
            a: Hva?
            a: Come again?
            
            
    state: BookAWorkplace || modal = true
        q!: * (book | workplace) *
        a: Would you like to book a workplace?
        
        state: Confirm
            q: * (yes | sure) *
            a: Well, you cannot, we aren't open yet!
            go: /Bye
            
        state: Decline
            q: * (no | not) *
            a: Oh, well...
            go!: /Bye/ByeBye
            
        state: LocalCatchAll
            event: noMatch
            a: Just yes or no please!
            
            
    state: Bye
        
        state: ByeBye
            event: noMatch
            random: 
                a: Bye!
                a: See ya!
                a: Salut!
                