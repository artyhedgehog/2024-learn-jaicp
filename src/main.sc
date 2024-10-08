require: ./patterns.sc

theme: /
    
    init:
        bind('postProcess', function ($context) {
            log('DEBUG: $currentState: ' + $context.currentState);
            log('DEBUG: $context: ' + toPrettyString($context));
            
            $context.session.lastState = $context.currentState;
        });
    
    state: Start
        q!: *start
        q!: * $hello *
        q: * $cancel * || fromState = /Book 
        q: * $cancel * || fromState = /Pay 
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
        go!: {{$session.lastState}}
            
    state: Book || modal = true
        a: What would you like to book?
        buttons:
            "A workplace" -> Workplace
            "A meeting room" -> MeetingRoom
            "An auditorium" -> Auditorium
    
        state: Workplace
            a: There is a desk with a monitor and an armchair.
            script:
                $session.bookType = 'workplace';
            go!: /Pay
            
        state: MeetingRoom
            a: We have a nice hall with a round table!
            script:
                $session.bookType = 'meetingRoom';
            go!: /Pay
            
        state: Auditorium
            a: Unfortunately, we don't have any available.

        state: LocalCatchAll
            event: noMatch
            a: I don't think we have that
            go!: ..
            
                
    state: Pay || modal = true
        a: How would you like to pay?
        if: $request.channelType === 'telegram'
            inlineButtons:
                {text: "Bank card", url: "https://alfabank.ru/"}
                {text: "Cash", url: "https://alfabank.ru/atm/map"}
        else:
            buttons:
                "Bank card"
                "Cash"
            
        state: Any
            q: * ($payCard|$payCash) *
            script:
                $temp.botName = capitalize($injector.botName)
                log('DEBUG: /Start: $parseTree: ' + toPrettyString($parseTree));

                var payWith = $parseTree._payCard && 'Bank card' || $parseTree._payCash && 'Cash';
                
                log('DEBUG: pickedOption: ' + payWith);
                
                $context.session.payWith = payWith;

            a: You can pay in place
            a: How many places do you need?
            
            state: ConfirmBooking
                q: * @duckling.number *
                script:
                    var places = $parseTree['_duckling.number'];
                    
                    var answer = $session.bookType === 'workplace' 
                        ? "You've booked " + places + ' ' + $nlp.conform('workplace', places)
                        : "You've booked a meeting room for " + places + ' ' + $nlp.conform('person', places);
                    
                    $reactions.answer(answer);
            
        state: CatchAllLocal
            q: noMatch
            a: Sorry, this isn't supported.
            go!: ..
