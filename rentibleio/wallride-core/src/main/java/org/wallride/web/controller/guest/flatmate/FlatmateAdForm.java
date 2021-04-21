package org.wallride.web.controller.guest.flatmate;

import lombok.Getter;
import lombok.Setter;
import org.wallride.domain.UserDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FlatmateAdForm implements Serializable {

	private Long id;
	private String title;
	private Integer budget;
	private UserDetail userDetail;
	private String currencyCaption;
	private String termOfLease;
	private List<String> districts = new ArrayList<>();

}
