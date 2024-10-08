require: ./patterns.sc

theme: /
    
    state: Start
        q!: *start
        q!: $hello
        q: * (~no [~thank]|~cancel|~stop|~quit|~exit) * || fromState = /Book 
        random:
            a: Hello, {{$request.channelType}}!
            a: Greetings, {{$request.channelType}}!
            a: Who's there in {{$request.channelType}}?!
        go!: /Book
        
    state: CatchAll || noContext = true
        event!: noMatch
        random:
            a: Forstor ikke, men veldig interessant!
            a: Hva?
            a: Come again?
        random:
            a: Could you try rephrasing?
            a: Can you repeat in other words?
            
    state: Book || modal = true
        a: What would you like to book?
        buttons:
            "A workplace" -> Workplace
            "A meeting room" -> MeetingRoom
            "An auditorium" -> Auditorium
    
        state: Workplace
            a: There is a desk with a monitor and an armchair.
            go!: /Pay
            
        state: MeetingRoom
            a: We have a nice hall with a round table!
            go!: /Pay
            
        state: Auditorium
            a: Unfortunately, we don't have any available.

        state: LocalCatchAll
            event: noMatch
            a: I don't think we have that
            go!: ..
            
                
    state: Pay || modal = true
        a: How would you like to pay?
        script:
            $temp.buttons = {
                bankCard: {
                    text: "Bank card",
                    url: 'https://alfabank.ru/',
                },
                cash: {
                    text: 'Cash',
                    url: 'https://alfabank.ru/atm/map',
                },
            }
        if: $request.channelType === 'telegram'
            inlineButtons:
                {{$temp.buttons.bankCard}}
                {{$temp.buttons.cash}}
        else:
            buttons:
                "Bank card"
                "Cash"
            
        state: Any
            q: * (~card | ~cash) *
            a: You can pay in place
            
        state: CatchAllLocal
            q: noMatch
            a: Sorry, this isn't supported.
            go!: ..
