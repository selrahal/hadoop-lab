#!/usr/bin/python
# 
# Generate transaction files
# Format: Credit Card #, City, State, Amount 

import random
import logging
from optparse import OptionParser

def read_options():
    parser = OptionParser()
    parser.add_option("-f", "--logfile", dest="logfile", help="Specify a log file. Default=/var/log/transactions.log", default="/var/log/transactions.log", type="string")
    parser.add_option("-n", "--number-of-iterations", dest="iterations", help="Specify the number of log events to create. Default=100", default="100", type="int")
    (options, args) = parser.parse_args()
    return options

def get_city_state(states):
    state = random.choice(states.keys())
    city = random.choice(states[state])
    return state,city 

def get_price():
    return round(random.random() * 100 + random.randint(0,10),2)

def get_credit_card(credit_cards):
    return random.choice(credit_cards)

def main():
    options = read_options()
    logfile = options.logfile
    iterations = options.iterations
    logging.basicConfig(filename=options.logfile,
                        format='%(message)s',
                        level=logging.DEBUG)
    credit_cards = ['1234123412341234', '4356345634563456', '7645456745674567']
    states = { 'NC':['Durham', 'Charlotte', 'Raleigh'], 'OR':['Portland', 'Salem'], 'FL' : ['Tampa', 'St.Petersburg', 'Orlando'], 'DE' : ['Wilmington', 'Newark']}
    for x in range(iterations):
        state, city = get_city_state(states)
        price = get_price()
        credit_card = get_credit_card(credit_cards)
	logging.info('%s,%s,%s,%s'%(credit_card,city,state,price))


if __name__ == "__main__":
    main()
