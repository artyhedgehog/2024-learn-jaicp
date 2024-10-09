patterns:
    $hello = (~hello|~hi|~hola|~hei|~hallo|~hej)
    $cancel = (~no [~thank]|~cancel|~stop|~quit|~exit|~abort)
    $payCard = ([(~bank|~credit|~plastic)] * ~card|~plastic)
    $payCash = ~cash