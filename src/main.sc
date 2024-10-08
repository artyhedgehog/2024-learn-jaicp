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
        a: What would you like to book?
        buttons:
            "A workplace" -> Workplace
        
        state: Workplace || modal = true
            a: There is a desk with a monitor and an armchair. How would you like to pay?
            buttons:
                "Bank card"
                "Cash"
            
            state: Any
                q: * (~card | ~cash) *
                a: You can pay in place
                
            state: CatchAllLocal
                q: noMatch
                a: Sorry, this isn't supported.


        state: Decline
            q: * (~no | ~not) *
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
                