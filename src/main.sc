require: ./patterns.sc

theme: /
    
    init:
        bind('postProcess', function ($context) {
            log('currentState' + toPrettyString($context.currentState));
            log('contextPath' + toPrettyString($context.contextPath));

        });
    
    state: Start
        script:
            $temp.botName = capitalize($injector.botName)
        
        q!: *start
        q!: * $hello *
        q: * (~no [~thank]|~cancel|~stop|~quit|~exit) * || fromState = /Book 
        q: cancel || fromState = /CatchAll
        random:
            a: Hello, {{$request.channelType}}!
            a: Greetings, {{$request.channelType}}!
            a: Who's there in {{$request.channelType}}?!
        if: $temp.botName
            a: My name is {{$temp.botName}}!
        script:
            $response.replies = $response.replies || [];
            $response.replies.push({
                type: 'image',
                imageUrl: 'https://plus.unsplash.com/premium_photo-1661962361446-f450f3f21495?q=80&w=2572&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
                text: 'Coworking',
            });
        go!: /Book
        
    state: CatchAll || noContext = true
        event!: noMatch
        random:
            a: Come again?
            a: Could you try rephrasing?
            a: Can you repeat in other words?
        go: /Start
            
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
                {text: "Bank card", url: "https://alfabank.ru/"}
                {text: "Cash", url: "https://alfabank.ru/atm/map"}
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
