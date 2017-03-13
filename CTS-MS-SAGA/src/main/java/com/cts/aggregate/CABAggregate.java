package com.cts.aggregate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.cts.bo.CABReservationBO;
import com.cts.command.CABBookOrCancelCommand;
import com.cts.command.HotelBookOrCancelCommand;
import com.cts.event.CABBookOrCancelEvent;
import com.cts.event.CABReservationDoneEvent;
import com.cts.event.HotelReservationDoneEvent;

import lombok.NoArgsConstructor;

@Aggregate
@NoArgsConstructor
public class CABAggregate {
	
	@AggregateIdentifier
	private String cabBookingReferenceNumber;
	
	@CommandHandler
	public CABAggregate(CABBookOrCancelCommand command) {
		apply(new CABBookOrCancelEvent(command.getReservationBO()));
		apply(new CABReservationDoneEvent(command.getReservationBO()));// Notify
																			// the
																			// saga
																			// to
																			// move
																			// ahead
	}

	@EventSourcingHandler
	protected void on(HotelBookOrCancelCommand event) {
		this.cabBookingReferenceNumber = ((CABReservationBO) event.getReservationBO())
				.getReservationId();

	}

	@EventSourcingHandler
	protected void on(HotelReservationDoneEvent event) {
		this.cabBookingReferenceNumber = ((CABReservationBO) event.getResevationBO())
				.getReservationId();

	}

}
