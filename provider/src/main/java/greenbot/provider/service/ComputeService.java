package greenbot.provider.service;

import java.util.List;


import greenbot.rule.model.Compute;
import greenbot.rule.model.Tag;

public interface ComputeService {
	List<Compute> list(Tag excluded);
}
